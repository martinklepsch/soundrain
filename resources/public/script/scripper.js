var DURATION = 300;

var currently_processed_data;
var index_in_array_of_results;
var most_recent_drop_div;


$(document).ready(function() {
  $("#search-form").submit(function() {
    // Set button to "Loading..."
    var soundcloud_uri = $("#search").val();
    if(!is_valid_url(soundcloud_uri)) {
      show_error("Please enter a valid Soundcloud URL.");
      return;
    } else {
      $("#search-button").button('loading');
    }

    sc_uri = soundcloud_uri.split("/").slice(3).join("/")

    hide_error();
    $.ajax({
      url: sc_uri,
      datatype: "json",
      type: "GET",
      success: show_results
    })
    return false;
  });
});

function is_valid_url(str) {
  var pattern = /^(https|http):\/\/(www\.)?soundcloud\.com/i;
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
  console.log(data);
  out = $(".search-results");
  out.html("");

  // to reuse the data afterwards for drag'n'drop events
  currently_processed_data = data;

  for(var i=0; i<data.length; i++) {
    out.append(data[i].html);
    var new_id = ""+i;
    $(".drag").last().attr("id", new_id);
    setup_drag_n_drop(new_id);
  }
  // Remove "Loading..." from search-button
  $("#search-button").button('reset');
}

function setup_drag_n_drop(id) {
  id = document.getElementById(id);
  id.addEventListener("dragenter", dragEnter, false);
  id.addEventListener("dragleave", dragLeave, false);
  id.addEventListener("dragover", dragOver, false);
  id.addEventListener("drop", drop, false);
}

function dragEnter(evt) {
  evt.stopPropagation();
  evt.preventDefault();
  $(evt.target).removeClass("drag-inactive").addClass("drag-active");
}

function dragLeave(evt) {
  evt.stopPropagation();
  evt.preventDefault();
  $(evt.target).removeClass("drag-active").addClass("drag-inactive");
}

function dragOver(evt) {
  evt.stopPropagation();
  evt.preventDefault();
}

function drop(evt) {
  evt.preventDefault();
  $(evt.target).removeClass("drag-active").addClass("drag-inactive");
  most_recent_drop_div = evt.target;

  var files = evt.dataTransfer.files;
  var count = files.length;
  var file = files[0];

  var reader = new FileReader(), filter = /^(audio\/mp3|audio\/mpeg3|audio\/x\-mpeg\-3|video\/mpeg|video\/x\-mpeg3)$/i;
  reader.onload = handle_binary_data;

  // Abort, when the user hasn't uploaded anything
  if (count <= 0) {return; }
  // Check if the file is a valid mp3
  if (!filter.test(file.type)) { show_error("Please only upload mp3s"); return; }
  // Otherwise hide the error
  hide_error();

  reader.readAsBinaryString(file);
}

function handle_binary_data(evt) {
  var data_binary_string = evt.target.result;
  index_in_array_of_results =  parseInt($(most_recent_drop_div).attr("id"));
  // to binary string
  var tag = atob(currently_processed_data[index_in_array_of_results].tag);
  var mp3 = tag + data_binary_string;
  var mp3_data_uri = "data:audio/mp3;base64," + btoa(mp3);
  window.open(mp3_data_uri,'_blank');
}
