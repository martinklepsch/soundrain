$(document).ready(function() {
  $(".btn").click(function() {
    var input = $(".span5").val();
    if(!validURL(input)) {
      $(".form-search.control-group").addClass("error");
      $(".help-inline").html("Please enter a valid Soundcloud URL.");
      return;
    }
    $(".form-search.control-group").removeClass("error");
    $(".help-inline").html("");
    $.ajax({
      url: "/search/",
      data: {url: input},
      datatype: "json",
      type: "GET",
      success: function(data){console.log(data[0]); $("div.form-search").html(data[0].html);}
    });
  }
    );
});

function validURL(str) {
  var pattern = /https:\/\/(www\.)?soundcloud\.com/i;
  if(!pattern.test(str)) {
    alert("Please enter a valid URL.");
    return false;
  } else {
    return true;
  }
}