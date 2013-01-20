var DURATION = 300;

$(document).ready(function() {
  $(".btn").click(function() {
    var input = $("#search").val();
    if(!is_valid_url(input)) {
      show_url_error();
      return;
    }
    hide_url_error();
    $.ajax({
      url: "/search/",
      data: {url: input},
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
  console.log(JSON.stringify(data)); 
  $("search-results").html(data[0].html);
}