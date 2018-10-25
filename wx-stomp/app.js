App({
  onLaunch: function () {
    this.initSocket()
  },
  initSocket: function () {

    var socketOpen = false

    function sendSocketMessage(msg) {
      console.log('send msg:' + msg)
      if (socketOpen) {
        wx.sendSocketMessage({
          data: msg
        })
      } else {
        socketMsgQueue.push(msg)
      }
    }

    var ws = {
      send: sendSocketMessage
    }

    wx.connectSocket({
      url: 'ws://127.0.0.1:8080/myEndpoint' 
    })
    wx.onSocketOpen(function (res) {
      socketOpen = true
      ws.onopen()
    })

    wx.onSocketMessage(function (res) {
      console.log(res)
      ws.onmessage(res)
    })

    var Stomp = require('stomp.min.js').Stomp;
    Stomp.setInterval = function () { }
    Stomp.clearInterval = function () { }
    var client = Stomp.over(ws);

    client.connect({}, function (sessionId) {

      let openid = wx.getStorageSync('openid');
      console.log('openid = ' + JSON.stringify(openid));

      //subscribe topic
      client.subscribe('/topic/sendTo0', function (body, headers) {
        console.log('From MQ /topic/greetings:', body);
      });
      client.send('/messageMapping0', { 'openid': openid }, "I'm topic!");

      // subscribe queue 
      client.subscribe('/user/' + openid + '/message', function (body, headers) {
        wx.vibrateLong()
        console.log('From MQ to user /user/' + openid + '/message:', body);
      });
      client.send('/messageMapping1', { 'openid': openid }, "I'm queue!");
    })
    //send message to fire topic
  }
})
