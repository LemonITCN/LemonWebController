function TuanItemModel(url , title){
    this.url = url;
    this.title = title;
}
var tuanBrowser = new Browser();
var subBrowser = new Browser();
var detailBrowser = new Browser();
tuanBrowser.setSize(1440,4000);
subBrowser.setSize(1440,4000);
detailBrowser.setSize(1440,2000);
DataCollection.init('shopType,onlineStartTime,onlineEndTime,sellerName,sellerUrl');

var detailTimeoutChecker;

var tuanData = new Array();
var subData = new Array();
var detailData = new Array();

var subIndex = 0;
var detailIndex = -1;

tuanBrowser.setOnLoadSuccess(function(url){
    Log.success('B浏览器加载URL成功 : ' + url);
});
tuanBrowser.show();

function grabTuanList(){
    const listSelector = '#floor2 > div.ju-itemlist > ul';
    const aSelectorPart1 = ' > li:nth-child(';
    const aSelectorPart2 = ') > div > a';
    var lisArr = tuanBrowser.dataGet.getChildren(listSelector);
    Log.success('今日开团的数量为：' + lisArr.length + '，准备采集！');
    for (var i = 0 ; i < lisArr.length ; i ++){
        Log.success(listSelector + aSelectorPart1 + i + aSelectorPart2);
        var tUrl = tuanBrowser.dataGet.getADomURL(listSelector + aSelectorPart1 + i + aSelectorPart2);
        var tTitle = lisArr[i].title;
        tuanData.push(new TuanItemModel(tUrl, tTitle));
    }
    Log.success("采集任务执行完毕");
    tuanBrowser.close();
    grabSubNext();
}

subBrowser.setOnClose(function(){
    grabSubNext();
});

function grabSubNext(){
    subIndex ++;
    if (subIndex == 4 /*== tuanData.length*/){
        Log.success('所有SUB任务采集完毕!');
        console.log('All task grab complete');
        DataCollection.flush();
	subBrowser.close();
        Communication.call('task' , 'success');
        return;
    }
    subBrowser.show();
    subBrowser.operate.loadURL(tuanData[subIndex].url , function(){
        setTimeout('grabSubList()' , 1000);
    },function(){
        Log.error("URL加载失败！！！" + url );
    });
}

function grabSubList(){
    Log.success('采集数据开始');
    subBrowser.operate.bufferAllScreen(800,function(){
        var t1s = subBrowser.dataGet.getNodesWithClassName('item-big-v2');
        var t2s = subBrowser.dataGet.getNodesWithClassName('item-small-v3');
        Log.success('当前页面共发现大方框抢购项目：' + t1s.length + '条 ， 小方框抢购项目:' + t2s.length + '条');
        subData = t2s;
        detailIndex = -1;
        grabDetailNext();
    });
}

detailBrowser.setOnClose(function(){
    grabDetailNext();
});

function grabDetailNext(){
    detailIndex ++;
    if (detailIndex >= subData.length){
        Log.success('当前SUB任务采集完毕！');
        subBrowser.close();
        return;
    }
    detailBrowser.show();
    detailTimeoutChecker = setTimeout(function(){
        Log.error("超时！！强制执行！！");
        grabDetailInfo();
    }, 5000);
    Log.info("当前子元素数量：" + subData.length + " current : " + detailIndex);
    var dUrl;
    try{
        dUrl = subData[detailIndex].getElementsByClassName('link-box')[0].href;
        Log.info("打开指定URL：" + dUrl);
        detailBrowser.operate.loadURL(dUrl , function(){
            grabDetailInfo();
        }, function(){
            Log.error("无法加载详情页面！");
        })  
    }catch(e){Log.error('获取dURL出错了！！！');} 
}

function grabDetailInfo(){
    clearTimeout(detailTimeoutChecker);
    shopType = detailBrowser.executeJavaScript('JU_DETAIL_DYNAMIC.shopType');
    onlineStartTime = detailBrowser.executeJavaScript('JU_DETAIL_DYNAMIC.onlineStartTime;');
    onlineEndTime = detailBrowser.executeJavaScript('JU_DETAIL_DYNAMIC.onlineEndTime;');
    sellerName = detailBrowser.dataGet.getInnerText('.sellername');
    sellerUrl = detailBrowser.dataGet.getADomURL('.sellername');
    Log.success('采集成功 ： ' + JSON.stringify(shopType));
    DataCollection.addRowItems([shopType,onlineStartTime , onlineEndTime , sellerName , sellerUrl]);
    detailBrowser.close();
}

tuanBrowser.operate.loadURL('https://ju.taobao.com/jusp/tongzhuangpindao/tp.htm',function(){
    tuanBrowser.operate.bufferAllScreen(800,function(){
		grabTuanList()
    });
},function(){
    Log.error("无法加载主页面！");
});
