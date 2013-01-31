var DURATION = 300;

$(document).ready(function() {
  $("#search-button").click(function() {
    // Set button to "Loading..."
    $(this).button('loading');
    var soundcloud_uri = $("#search").val();
    if(!is_valid_url(soundcloud_uri)) {
      show_url_error();
      return;
    }

    sc_uri = soundcloud_uri.split("/").slice(3).join("/")

    hide_url_error();
    $.ajax({
      url: sc_uri,
      datatype: "json",
      type: "GET",
      success: show_results
    });
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
  for(var i=0; i<data.length; i++) {
    out.append(data[i].html);
  }
  // Remove "Loading..." from search-button
  $("#search-button").button('reset');
  load_mp3(data[0].mp3);
}

function load_mp3(url) {
  console.log("entering mp3 loader");
  $.ajax({
    type: "get",
    url: url,
    dataType: "arraybuffer",
    crossDomain: true,
    done: (function() {alert("done");}),
    always: (function() {alert("complete");}),
    fail: (function() {alert("complete");}),
  });
  console.log("leaving mp3 loader");
}
