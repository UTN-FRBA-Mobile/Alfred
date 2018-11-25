import SimpleSchema from  'simpl-schema';
import { Mongo } from 'meteor/mongo';

export const Risks = new Mongo.Collection('risks');

const risksSchema = new SimpleSchema({
  userId: {
    type: String,
    label: "userId"
  },
  risk: {
    type: String,
    label: "risk"
  },
  probability: {
    type: String,
    label: "probability"
  },
  impact: {
    type: String,
    label: "impact"
  },
  detectionCapacity: {
    type: String,
    label: "detectionCapacity"
  },
  userTasksId: {
    type: String,
    label: "userTasksId",
    optional: true
  },
  createdAt: {
    type: Date,
    autoValue: function() { if (!this.isSet) return new Date(); else return undefined; },
  }
});

Risks.insertRisks = (risks) => {
  Risks.remove({userId: Meteor.userId()});
  risks.forEach(risk => {
    const newRisk = Object.assign({}, risk);
    newRisk.userId = Meteor.userId();
    Risks.insert(newRisk);
  });
};

Risks.updateUserTaskId = (_id, userTasksId) => {
  Risks.update({_id}, {$set: {userTasksId}});
};

Risks.removeUserTaskIds = () => {
  Risks.update({userId: Meteor.userId()}, {$unset: {userTasksId: ""}}, {multi: true});
};

Risks.attachSchema(risksSchema);
