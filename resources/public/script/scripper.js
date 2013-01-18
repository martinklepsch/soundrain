$(document).ready(function() {
  $(".btn").click(function() {
    var $input = $(".span5");
    $.ajax({
      url: "/search/",
      data: {url: $input.val()},
      datatype: "json",
      type: "GET",
      success: function(data){console.log(data[0]); $("div.form-search").html(data[0].html);}
    });
  }
    );
});