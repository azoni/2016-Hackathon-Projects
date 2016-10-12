'use strict';

var AlexaSkill = require('./AlexaSkill');
var AWS = require("aws-sdk");
var APP_ID = undefined; 
AWS.config.update({
  region: "us-east-1",
  endpoint: "https://dynamodb.us-east-1.amazonaws.com",
  accessKeyId:"",//hidden
  secretAccessKey:""//hidden
});
var docClient = new AWS.DynamoDB.DocumentClient();

/**
 * FreshBox is a child of AlexaSkill.
 * To read more about inheritance in JavaScript, see the link below.
 *
 * @see https://developer.mozilla.org/en-US/docs/Web/JavaScript/Introduction_to_Object-Oriented_JavaScript#Inheritance
 */
var FreshBox = function () {
    AlexaSkill.call(this, APP_ID);
    this.usernumber = 0;
    this.username = "";
};
function SessionInitializer(session, callback) {
  console.log("starting launch application");
  var table = "pantry_users";
  var params = {
  TableName: table,
    Key:{
        "user_id": session.user.userId
    }
  };
  console.log("fetching data from the table.");
  docClient.get(params, function(err, data) {
    if (err) {
      console.error("Unable to read item. Error JSON:", JSON.stringify(err, null, 2));
    } else {
      var user_data =  JSON.stringify(data, null, 2);
      var jsonData = JSON.parse(user_data);
      if(jsonData.Item) {
        session.attributes.userFoods = [];
        if(jsonData.Item.pantry_foods) {//user has foods , store in session attributes
          session.attributes.userFoods = jsonData.Item.pantry_foods;
        }
        callback(session);
      } else {//if user not found create a new entry
        //need to insert user into database and update pk, then run as usual
        var params = {
            TableName:table,
            Item:{
                "user_id": session.user.userId
            }
        };
        console.log("Adding a new item...");
        docClient.put(params, function(err, data) {
            if (err) {
                console.error("Unable to add item. Error JSON:", JSON.stringify(err, null, 2));
            } else {
                console.log("Added item:", JSON.stringify(data, null, 2));
                session.attributes.userFoods = [];
                callback(session);
            }
        });
      }
    }
  });
}
var updateItems = function(session) {
  var table = "pantry_users";
  var params = {
    TableName:table,
    Key:{
        "user_id": session.user.userId
    },
    UpdateExpression: "set pantry_foods = :cur_items",
    ExpressionAttributeValues:{
        ":cur_items":session.attributes.userFoods
    },
    ReturnValues:"UPDATED_NEW"
  };
  console.log("Updating the item...");
  docClient.update(params, function(err, data) {
      if (err) {
          console.error("Unable to update item. Error JSON:", JSON.stringify(err, null, 2));
      } else {
          console.log("UpdateItem succeeded:", JSON.stringify(data, null, 2));
      }
  });
};
function ModifyResponse (itemData, itemName, response, session, callback) {
      var cardTitle = itemName,
          speechOutput,
          repromptOutput;
      if (itemData) {
          speechOutput = {
              speech: itemData,
              type: AlexaSkill.speechOutputType.PLAIN_TEXT
          };
          response.tellWithCard(speechOutput, cardTitle, "recipe");
          if(callback) {
            callback(session);
          }
      } else {
          var speech;
          if (itemName) {
              speech = "you need to pick up some " +  itemName + ". What else can I help with?";
          } else {
              speech = "I'm sorry, I currently do not know that item. What else can I help with?";
          }
          speechOutput = {
              speech: speech,
              type: AlexaSkill.speechOutputType.PLAIN_TEXT
          };
          repromptOutput = {
              speech: "What else can I help with?",
              type: AlexaSkill.speechOutputType.PLAIN_TEXT
          };
          response.ask(speechOutput, repromptOutput);
      }
}
// Extend AlexaSkill
FreshBox.prototype = Object.create(AlexaSkill.prototype);
FreshBox.prototype.constructor = FreshBox;
FreshBox.prototype.eventHandlers.onLaunch = function (launchRequest, session, response) {
  var speechText = "Welcome to your pantry";
  console.log(speechText);
  var repromptText = "For instructions on what you can say, please say help me.";
  response.ask(speechText, repromptText);
};
FreshBox.prototype.eventHandlers.onSessionStarted = function (sessionStartedRequest, session) {//load all user data from database
    console.log("a new session has started");

};

FreshBox.prototype.intentHandlers = {
    "QuantityIntent": function (intent, session, response) {
      var itemSlot = intent.slots.Item,
          itemName;
      if (itemSlot && itemSlot.value){
          itemName = itemSlot.value.toLowerCase();
      }
      var findItem = function(session) {
        var itemData = null;
        for(var i = 0; i < session.attributes.userFoods.length; i++) {
          if (session.attributes.userFoods[i].name === itemName) {
            itemData = "you currently have ";
            itemData += session.attributes.userFoods[i].quantity + " " + session.attributes.userFoods[i].name;
            break;
          }
        }
        ModifyResponse(itemData, itemName, response);
      };
    if(session.attributes.userFoods) {//user is already in the system
      findItem(session);
      //console.log("user data has already been loaded " + session.attributes.user);
    } else {
      SessionInitializer(session, findItem);
    }
  },
  "RemoveIntent": function (intent, session, response) {
      var itemSlot = intent.slots.Item,
          itemName;
      var numberSlot = intent.slots.Number,
          number;
      if (itemSlot && itemSlot.value){
          itemName = itemSlot.value.toLowerCase();
      }
      if(numberSlot && numberSlot.value) {
        number = numberSlot.value;
      }
      var removeItem = function(session) {
        var itemData = null;
        for(var i = 0; i < session.attributes.userFoods.length; i++) {
          if (session.attributes.userFoods[i].name === itemName && number) {
            if(session.attributes.userFoods[i].quantity >=  number) {
              session.attributes.userFoods[i].quantity -= number;
              itemData = "you now have " + session.attributes.userFoods[i].quantity + " " + itemName;
            } else {
              itemData = "you do not have enough " + itemName;
            }
            break;
          }
        }
        ModifyResponse(itemData, itemName, response, session,updateItems);
      };
      if(session.attributes.userFoods) {//user is already in the system
        removeItem(session);
      } else {
        SessionInitializer(session, removeItem);
      }
    },
    "AddIntent": function (intent, session, response) {
        var itemSlot = intent.slots.Item,
            itemName;
        var numberSlot = intent.slots.Number,
            number;
        if (itemSlot && itemSlot.value){
            itemName = itemSlot.value.toLowerCase();
        }
        if(numberSlot && numberSlot.value) {
          number = numberSlot.value;
        }
        var addItem = function(session) {
          var itemData = null;
          for(var i = 0; i < session.attributes.userFoods.length; i++) {
            if (session.attributes.userFoods[i].name === itemName && number) {
              session.attributes.userFoods[i].quantity = Number(session.attributes.userFoods[i].quantity) + Number(number);
              itemData = "you now have " + session.attributes.userFoods[i].quantity + " " + itemName;
              break;
            }
          }
          if(!itemData && itemName && number) {//user didn't have any of the item, append to sessionAttributes
            session.attributes.userFoods.push({name:itemName, quantity:number});
            itemData = "you now have " + number + " " + itemName;
          }
          ModifyResponse(itemData, itemName, response, session,updateItems);
        };
        if(session.attributes.userFoods) {//user is already in the system
          addItem(session);
        } else {
          SessionInitializer(session, addItem);
        }
    },
    "AMAZON.StopIntent": function (intent, session, response) {
        var speechOutput = "Goodbye";
        response.tell(speechOutput);
    },

    "AMAZON.CancelIntent": function (intent, session, response) {
        var speechOutput = "Goodbye";
        response.tell(speechOutput);
    },

    "AMAZON.HelpIntent": function (intent, session, response) {
        var speechText = "You can ask questions about the quantity of your items stored in your pantry, or add items to your kitchen, or, you can say exit... Now, what can I help you with?";
        var repromptText = "You can say things like, how many bananas do i have, or add three bananas, or you can say exit... Now, what can I help you with?";
        var speechOutput = {
            speech: speechText,
            type: AlexaSkill.speechOutputType.PLAIN_TEXT
        };
        var repromptOutput = {
            speech: repromptText,
            type: AlexaSkill.speechOutputType.PLAIN_TEXT
        };
        response.ask(speechOutput, repromptOutput);
    }
};

exports.handler = function (event, context) {
    var freshbox = new FreshBox();
    freshbox.execute(event, context);
};
