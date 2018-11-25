import SimpleSchema from  'simpl-schema';
import { Mongo } from 'meteor/mongo';

export const Swots = new Mongo.Collection('swots');

const swotsSchema = new SimpleSchema({
  userId: {
    type: String,
    label: "userId"
  },
  type: {
    type: String,
    label: "type"
  },
  userTasksId: {
    type: String,
    label: "userTasksId",
    optional: true
  },
  description: {
    type: String,
    label: "description",
    optional: true
  },
  descriptions: {
    type: Array,
    optional: true,
    label: "descriptions"
  },
  'descriptions.$': {
    type: String,
  },
  genericFodaElementId: {
    type: String,
    label: "genericFodaElementId",
    optional: true
  },
  createdAt: {
    type: Date,
    autoValue: function() { if (!this.isSet) return new Date(); else return undefined; },
  }
});

Swots.insertSwot = (swot, userId = Meteor.userId()) => {
  Swots.remove({userId: userId});
  console.log(swot);
  ['strengths', 'weaknesses', 'opportunities', 'threats'].forEach(swotElement => {
    swot[swotElement].forEach(element => {
      const newSwotElement = {};
      newSwotElement.userId = userId;
      newSwotElement.type = swotElement;
      newSwotElement.description = element;
      Swots.insert(newSwotElement);
    });
  });
};

Swots.updateUserTaskId = (_id, userTasksId) => {
  Swots.update({_id}, {$set: {userTasksId}});
};

Swots.removeUserTaskIds = () => {
  Swots.update({userId: Meteor.userId()}, {$unset: {userTasksId: ""}}, {multi: true});
};

Swots.getSwot = (userId = Meteor.userId() ) => {
  swotFound = Swots.find({userId: userId}).fetch();
  return swotFound
          ? swotFound
          : [];
};

Swots.attachSchema(swotsSchema);
