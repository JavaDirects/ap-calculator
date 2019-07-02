<?php
//Created By JumHuang
//Date 2019-4-5 11:53

$n1=$_REQUEST["n1"];
$n2=$_REQUEST["n2"];
$t=$_REQUEST["t"];

$result;

//Tips: -$n1或-$n2为$n1或$n2的相反数

if(!empty($n1)&!empty($n2)&!empty($t)) {
//t=1,2,3,4,5 分别为 加法，减法，乘法，除法，取余
if($t=="1") {
//加法计算
$result=$n1+$n2;
} elseif($t=="2") {
//减法计算
$result=$n1-$n2;
} elseif($t=="3") {
//乘法计算
$result=$n1*$n2;
} elseif($t=="4") {
//除法计算，不含小数，如9÷3=3
$result=$n1/$n2;
} elseif($t=="5") {
//除法计算，含小数，如9÷3=3.0
$result=$n1%$n2;
}
echo $result;
} else {
echo "数据传入不完整！";
}
?>