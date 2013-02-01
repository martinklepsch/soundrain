var DURATION = 300;

var current_data;

$(document).ready(function() {
  $(".form-search").submit(function() {
    // Set button to "Loading..."
    var soundcloud_uri = $("#search").val();
    if(!is_valid_url(soundcloud_uri)) {
      show_url_error();
      return;
    } else {
      $("#search-button").button('loading');
    }

    sc_uri = soundcloud_uri.split("/").slice(3).join("/")

    hide_url_error();
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
  var pattern = /https:\/\/(www\.)?soundcloud\.com/i;
  if(!pattern.test(str)) {
    // alert("Please enter a valid URL.");
    return false;
  } else {
    return true;
  }
}

function show_url_error() {
  $(".form-search.control-group").addClass("error");
  $(".help-inline").html("Please enter a valid Soundcloud URL.");
  $(".help-inline").show(DURATION);
}

function hide_url_error() {
  $(".form-search.control-group").removeClass("error");
  $(".help-inline").hide(DURATION);
  $(".help-inline").html("");
}

function show_results(data) {
  console.log(data);
  out = $(".search-results");
  out.html("");
  // to reuse the data afterwards for drag'n'drop events
  current_data = data;
  for(var i=0; i<data.length; i++) {
    out.append(data[i].html);
    $(".drag").last().attr("id", "drag-"+i);
  }
  // Remove "Loading..." from search-button
  $("#search-button").button('reset');
}
