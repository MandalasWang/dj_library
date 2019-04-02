<script>
function $(v){return document.getElementById(v);}
function jia(x){
var y=parseInt($("a").value);
if (x==0){y=y-1;}else{y=y+1;}
$("a").value=y;
}
var d = new Date();
var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
$("ri").value=str;
</script>