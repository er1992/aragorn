$(document).ready(function() {
  function fastAnim() {
    $("#t-val1").text(parseInt($("#t-val1").text())+1);
  }

  function midAnim() {
    $("#t-val2").text(parseInt($("#t-val2").text())+1);
    $("#t-val3").text(parseInt($("#t-val3").text())+1);
  }

  function slowAnim() {
    $("#t-val4").text(parseInt($("#t-val4").text())+1);
    $("#t-val5").text(parseInt($("#t-val5").text())+1);
  }
  function runner() {
      fastAnim();
      setTimeout(function() {
          runner();
      }, 500);
  }
  runner();

  function runner2() {
      midAnim()
      setTimeout(function() {
          runner2();
      }, 1500);
  }
  runner2();

  function runner3() {
      slowAnim()
      setTimeout(function() {
          runner3();
      }, 3000);
  }
  runner3();
});
