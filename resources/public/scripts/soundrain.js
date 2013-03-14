var DURATION = 300;
// size of the filesytstem that's going to be allocated
var FILESYSTEM_SIZE = 100000000;

// variables for data sharing between methods
var filesystem;
var currently_processed_data;
var currently_processed_files;

// regexes
var mp3_mime_type_regex = /^(audio\/mpeg|audio\/mp3|audio\/mpeg3|audio\/x\-mpeg\-3|video\/mpeg|video\/x\-mpeg3)$/i;
var url_regex = /^https:\/\/(www\.)?soundcloud\.com/i;



// Things to be done after jQuery loads
$(function() {
  // For generality's sake
  window.requestFileSystem = window.requestFileSystem || window.webkitRequestFileSystem;

  // Initializes the dropzone
  setup_drag_n_drop("dropzone");

  check_for_browser_support();

  $("#search-form").submit(function() {
    var soundcloud_uri = $("#search").val();
    if(!is_valid_url(soundcloud_uri)) {
      show_error("Please enter a valid Soundcloud URL.");
      return false;
    } else {
      hide_error();
      $("#search-button").button('loading');
    }

    sc_uri = soundcloud_uri.split("/").slice(3).join("/")

    $.ajax({
      url: sc_uri,
      datatype: "json",
      type: "GET",
    }).done(function(data) {
      hide_error();
      show_results(data);
      attach_handlers_to_buttons();
    });
    return false;
  });
});

function check_for_browser_support() {
  if(window.requestFileSystem) {
    hide_error();
  } else {
    show_error("We think that your Browser doesn't support this website. Give the newest Google Chrome a try ;)");
  }
}

// When the user downloads something, the dropzone opens.
function attach_handlers_to_buttons() {
  $('.download-link').click(function(evt) {
    // var jquery_object = $(evt.target);
    if(!$(this).attr('data-on-filesystem')){
      $('.drag').show(DURATION);
      $('.search-results').css('padding-bottom','120px');
      $(this).html("<i class='icon-hand-down'></i>now drag the downloaded file(s) into the box at the bottom of the page");
    }
  });
}

function is_valid_url(str) {
  return url_regex.test(str);
}

function show_error(msg) {
  $("#search-form.control-group").addClass("error");
  $(".help-inline").html(msg);
  $(".help-inline").show(DURATION);
  $("#search-button").button('reset');
}

function hide_error() {
  $("#search-form.control-group").removeClass("error");
  $(".help-inline").hide(DURATION);
  $(".help-inline").html("");
}

function show_results(data) {
  out = $(".search-results");
  out.html("");

  currently_processed_data = data;

  if (currently_processed_data.length <= 0) {
    show_error("Are you sure that the page exists and contains songs? We are not sure...")
    return;
  } 

  for(var i=0; i<currently_processed_data.length; i++) {
    out.append(currently_processed_data[i].html);
  }

  // Remove "Loading..." from search-button
  $("#search-button").button('reset');
}

function setup_drag_n_drop(id) {
  id = document.getElementById(id);
  id.addEventListener("dragenter", dragEnter, false);
  id.addEventListener("dragleave", dragLeave, false);
  // I have no idea why, but this dummy handler is necessary ;)
  id.addEventListener("dragover", dragOver, false);
  id.addEventListener("drop", drop, false);
}

function dragEnter(evt) {
  evt.stopPropagation();
  evt.preventDefault();
  $(evt.target).removeClass("drag-inactive").addClass("drag-active");
}

function dragOver(evt) {
  evt.stopPropagation();
  evt.preventDefault();
}

function dragLeave(evt) {
  evt.stopPropagation();
  evt.preventDefault();
  $(evt.target).removeClass("drag-active").addClass("drag-inactive");
}

function drop(evt) {
  evt.preventDefault();
  $(evt.target).removeClass("drag-active").addClass("drag-inactive");

  currently_processed_files = evt.dataTransfer.files;

  // request file system access
  window.requestFileSystem(window.TEMPORARY, FILESYSTEM_SIZE,
                           process_dropped_files,
                           error_handler);
}

function process_dropped_files(fs) {
  $("#search-button").button('loading');
  hide_error();

  filesystem = fs;
  var count = currently_processed_files.length;

  // Abort when the user hasn't uploaded anything
  if (count <= 0) {return; }

  for (var i=0; i<count; i++) {
    var reader = new FileReader();
    reader.onload = function(index) {
      return function(evt) {
        handle_binary_data(evt, index);
      };
    }(i);
    // Check if the file is a valid mp3
    if (!mp3_mime_type_regex.test(currently_processed_files[i].type)) {
      show_error("Please only upload MP3s");
      return;
    }
    // Otherwise hide the error
    hide_error();

    reader.readAsArrayBuffer(currently_processed_files[i]);
  }
}

function handle_binary_data(evt, current_index) {
  var right_index = -1;
  for (var i=0; i<currently_processed_data.length; i++) {
    if (currently_processed_files[current_index].name.indexOf(currently_processed_data[i].filename) != -1) {
      right_index = i;
    }
  }
  if (right_index == -1) {
    show_error("Please do not change the name of the downloaded files...");
    return;
  }
  hide_error();
  // to binary string
  // Copied code

  c = currently_processed_data[right_index];

  // var binary_tag = Base64Binary.decodeArrayBuffer(c.tag);

  var filename = (c.artist + " - " + c.title + ".mp3");

  // Actually write to the filesystem
  filesystem.root.getFile(filename, {create: true}, function(file_entry) {

    // Create a file_writer object for our file_entry (log.txt).
    file_entry.createWriter(function(file_writer) {

      file_writer.onwriteend = function(e) {
        var link = $('#mp3-'+right_index+' > .text > .download-link');
        link.attr("href", file_entry.toURL()).attr('download', filename);
        link.attr('data-on-filesystem', 'true').html("<i class='icon-download-alt'></i>download with metadata");
        var container = $('#mp3-'+right_index);
        container.addClass('ready');
        $("#search-button").button('reset');
        hide_error();
      };
      file_writer.onerror = function(e) {
        $("#search-button").button('reset');
        show_error('Write failed: ' + e.toString());
      };

      // Create a new Blob and write it to log.txt.
      var blob = ID3Writer.create([
                                    {frameType: 'TIT2', data: c.title},
                                    {frameType: 'TALB', data: c.album},
                                    {frameType: 'TPE1', data: c.artist},
                                    {frameType: 'TYER', data: c.year},
                                  ], evt.target.result);
      file_writer.write(blob);

    }, error_handler);
  }, error_handler);
}

// error handler for the filesystem access
function error_handler(e) {
  var msg = '';

  switch (e.code) {
    case FileError.QUOTA_EXCEEDED_ERR:
      msg = 'QUOTA_EXCEEDED_ERR';
      break;
    case FileError.NOT_FOUND_ERR:
      msg = 'NOT_FOUND_ERR';
      break;
    case FileError.SECURITY_ERR:
      msg = 'SECURITY_ERR';
      break;
    case FileError.INVALID_MODIFICATION_ERR:
      msg = 'INVALID_MODIFICATION_ERR';
      break;
    case FileError.INVALID_STATE_ERR:
      msg = 'INVALID_STATE_ERR';
      break;
    default:
      msg = 'Unknown Error';
      break;
  };

  show_error("Filesystem error: "+msg);
}
