var DURATION = 300;

var currently_processed_data;
var currently_processed_files;
var filter = /^(audio\/mpeg|audio\/mp3|audio\/mpeg3|audio\/x\-mpeg\-3|video\/mpeg|video\/x\-mpeg3)$/i;
var pattern = /^(https|http):\/\/(www\.)?soundcloud\.com/i;



$(function() {
  // Initializes the dropzone
  setup_drag_n_drop("dropzone");

  $("#search-form").submit(function() {
    // Set button to "Loading..."
    var soundcloud_uri = $("#search").val();
    if(!is_valid_url(soundcloud_uri)) {
      show_error("Please enter a valid Soundcloud URL.");
      return false;
    } else {
      $("#search-button").button('loading');
    }

    sc_uri = soundcloud_uri.split("/").slice(3).join("/")

    hide_error();
    $.ajax({
      url: sc_uri,
      datatype: "json",
      type: "GET",
    }).done(function(data) {
      show_results(data);
      attach_onclick_handlers_to_buttons();
    });
    return false;
  });
});

// When the user downloads something, the dropzone opens.
function attach_onclick_handlers_to_buttons() {
  $('.download-button').click(function(evt) {
    $('.drag').show(DURATION);
  });
}

function is_valid_url(str) {
  if(!pattern.test(str)) {
    // alert("Please enter a valid URL.");
    return false;
  } else {
    return true;
  }
}

function show_error(msg) {
  $("#search-form.control-group").addClass("error");
  $(".help-inline").html(msg);
  $(".help-inline").show(DURATION);
}

function hide_error() {
  $("#search-form.control-group").removeClass("error");
  $(".help-inline").hide(DURATION);
  $(".help-inline").html("");
}

function show_results(data) {
  out = $(".search-results");
  out.html("");

  for(var i=0; i<data.length; i++) {
    out.append(data[i].html);
  }

  // Remove "Loading..." from search-button
  $("#search-button").button('reset');

  // to reuse the data afterwards for drag'n'drop events
  currently_processed_data = data;
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
    if (!filter.test(currently_processed_files[i].type)) { show_error("Please only upload mp3s"); return; }
    // Otherwise hide the error
    hide_error();

    reader.readAsBinaryString(currently_processed_files[i]);
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
    show_error("This was an invalid mp3");
    return;
  }
  hide_error();
  // to binary string
  var mp3_data_uri = "data:audio/mp3;base64," + btoa(atob(currently_processed_data[right_index].tag) + evt.target.result);
  // crashes the browser
  window.open(mp3_data_uri,'_blank');
}
