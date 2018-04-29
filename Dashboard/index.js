var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var path = require('path');
var express = require('express');
var dir = path.join(__dirname, '.');
var mysql = require('mysql');
var mysql_events = require('mysql-events');
var dsn = {
  host: "localhost",
  user: "root",
  password: "vineet",
};
var mysqlEventWatcher = mysql_events(dsn);
var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "vineet",
  database: "rtos"
});

app.use(express.static(dir));
app.get('/', function(req, res){
  res.sendFile(__dirname + '/index.html');
});

io.on('connection', function(socket){
  var watcher = mysqlEventWatcher.add(
    'rtos.track_info',
    function (oldRow, newRow, event) {
      //row inserted
      if (oldRow === null) {
        //insert code goes here
        console.log("added");
        console.log(newRow);
        console.log(newRow.fields.uid);
        io.volatile.emit('chat message', newRow.fields.bssid);
      }

      //row deleted
      if (newRow === null) {
        //delete code goes here
        // console.log("added");
      }

      //row updated
      if (oldRow !== null && newRow !== null) {
        //update code goes here
        // console.log("added");
      }

      //detailed event information
      // console.log(event)
    },
    'match this string or regex'
  );
  console.log('a user connected');
  // socket.on('chat message', function(msg){
  //   console.log('message: ' + msg);
  // });
  socket.on('disconnect', function(){
    console.log('user disconnected');
    // watcher.remove();
    mysqlEventWatcher.stop();
  });
});
http.listen(3000, function(){
  console.log('listening on *:3000');
});
