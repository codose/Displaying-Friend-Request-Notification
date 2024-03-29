/*
this is the node.js that should be deployed to Firebase-Functions
the code being presented here is working fine on the day August 06,2018. */   
 'use strict' 
    const functions = require('firebase-functions'); 
    const admin = require('firebase-admin'); 
    admin.initializeApp(functions.config().firebase); 
    exports.sendNotification = functions.database.ref('/notifications/{user_id}/{notification_id}').onWrite((data, context) => { 
    const user_id = context.params.user_id; 
    const notification_id = context.params.notification_id; 
    console.log('We have a notification to send to  : ', user_id); 
    const deviceToken = admin.database().ref(`/Users/${user_id}/device_token`).once('value');
    return deviceToken.then(result=>{
    const token_id=result.val();
    const payload={
    notification:{
    title: "FRiend Request",
    body: "You have received a friend request",
    icon:"default"}
    };    
    return admin.messaging().sendToDevice(token_id,payload).then(response =>{
    console.log('This was notification feature');
    return true;});

    }); 
    });
