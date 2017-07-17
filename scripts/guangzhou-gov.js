var listBrowser = new Browser();
listBrowser.setSize(1280, 800);
listBrowser.show();
var detailBrowser = new Browser();
detailBrowser.setSize(1280, 800);

var allPageCount = 0;
var currentIndex = 1;
var detailIndex = -1;

var itemsUrlArr;

listBrowser.setOnLoadSuccess(function (url) {
    Log.success('list浏览器加载URL成功 : ' + url + " ，当前页码：" + currentIndex);
    if (currentIndex > 1) {
        grabCurrent();
    }
});

listBrowser.setOnClose(function () {
    changeToNext();
});

detailBrowser.setOnClose(function () {
    grabDetailNext();
});

listBrowser.operate.loadURL('http://www.gzg2b.gov.cn/_Layouts/ApplicationPages/News/News.aspx?ColumnID=6D13C10D-1BF3-4AA2-B739-CBDA18E17D33', function () {
    var ctrlButtons = listBrowser.executeJavaScript('document.getElementsByClassName(\'FirstLastButtons\')');
    allPageCount = ctrlButtons[ctrlButtons.length - 1].href.split('\'')[3];
    grabCurrent();
}, function () {
    Log.error('打开列表页面失败：' + currentIndex);
});

function grabCurrent() {
    Log.success('开始采集，页码：' + currentIndex);
    itemsUrlArr = new Array();
    var all = listBrowser.dataGet.getChildren('#aspnetForm > table > tbody > tr:nth-child(2) > td.lmr_middle2 > div.wp_main > ul');
    for (var i = 0; i < all.length; i++) {
        itemsUrlArr.push(all[i].getElementsByTagName('a')[0].href);
    }
    detailIndex = -1;
    grabDetailNext();
}

function changeToNext() {
    gotoPage(++currentIndex);
}

function gotoPage(pageNum) {
    listBrowser.executeJavaScript('document.getElementById(\'ctl00$main$pagerHeader_input\').value=' + pageNum);
    listBrowser.executeJavaScript('document.getElementById(\'ctl00$main$pagerHeader_btn\').click()');
    listBrowser.show();
}

function grabDetailNext() {
    if (++detailIndex >= itemsUrlArr.length) {
        listBrowser.close();
        return;
    }
    detailBrowser.show();
    detailBrowser.operate.loadURL(itemsUrlArr[detailIndex], function () {
        Log.success('采集成功：' + detailBrowser.dataGet.getInnerText('#note_container > div > div'));
        detailBrowser.close();
    }, function () {
        Log.error('详情页面加载失败:' + itemsUrlArr[detailIndex]);
    });
}