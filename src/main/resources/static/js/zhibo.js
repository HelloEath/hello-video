if(!zanpiancms_player.apiurl){
    zanpiancms_player.apiurl = '//cdn.97bike.com/api/?type='+zanpiancms_player.name+'&url=';
};
document.write('<iframe class="zanpiancms-play-iframe" id="buffer" src="//www.meijupa.com/m3u8.php?url='+zanpiancms_player.adurl+'" width="100%" height="100%" frameborder="0" scrolling="no" style="position:absolute;z-index:9;display:none"></iframe>');
document.write('<iframe class="zanpiancms-play-iframe" src="//www.meijupa.com/m3u8.php?url='+zanpiancms_player.url+'" width="100%" height="100%" frameborder="0" scrolling="no" allowfullscreen="true"></iframe>');
ads_show();
//http://www.meijupa.com/m3u8.php?url=https://yuledy.helanzuida.com/20200814/8960_3dd4fcd5/index.m3u8
function ads_show(){
    try{
        if(zanpiancms_player.adurl!=null && zanpiancms_player.adtime>0){
            document.getElementById("buffer").style.display = "block";
            setTimeout("document.getElementById(\"buffer\").style.display=\"none\"",zanpiancms_player.adtime*1000);
        }else{
            setTimeout(function(){ads_show();},200);
        }
    }catch(e){}
}