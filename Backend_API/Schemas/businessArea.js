import SimpleSchema from  'simpl-schema';
import { Mongo } from 'meteor/mongo';

export const BusinessAreas = new Mongo.Collection('businessAreas');

const competitorsSchema = new SimpleSchema({
  name: {
    type: String,
    label: "name",
    optional: true
  },
  differentiator: {
    type: String,
    label: "differentiator",
    optional: true
  }
});

const businessAreasSchema = new SimpleSchema({
  userId: {
    type: String,
    label: "userId"
  },
  name: {
    type: String,
    label: "name"
  },
  details: {
    type: String,
    label: "details",
    optional: true
  },
  partners: {
    type: String,
    label: "partners",
    optional: true
  },
  activities: {
    type: String,
    label: "activities",
    optional: true
  },
  resources: {
    type: String,
    label: "resources",
    optional: true
  },
  valueProposition: {
    type: String,
    label: "valueProposition",
    optional: true
  },
  relationships: {
    type: String,
    label: "relationships",
    optional: true
  },
  channels: {
    type: String,
    label: "channels",
    optional: true
  },
  segments: {
    type: String,
    label: "segments",
    optional: true
  },
  providers: {
    type: String,
    label: "providers",
    optional: true
  },
  clients: {
    type: String,
    label: "clients",
    optional: true
  },
  agglutinators: {
    type: String,
    label: "agglutinators",
    optional: true
  },
  income: {
    type: String,
    label: "income",
    optional: true
  },
  costs: {
    type: String,
    label: "costs",
    optional: true
  },
  competitors: {
    type: Array,
    label: "competitors",
    optional: true
  },
  'competitors.$': {
    type: competitorsSchema,
  },
  createdAt: {
    type: Date,
    autoValue: function() { if (!this.isSet) return new Date(); else return undefined; },
  }
});

BusinessAreas.insertBusinessAreas = (businessAreas, userId = Meteor.userId()  ) => {
  BusinessAreas.remove({userId: userId});
  businessAreas.forEach(businessArea => {
    const newBusinessArea = Object.assign({}, businessArea);
    newBusinessArea.userId = userId;
    BusinessAreas.insert(newBusinessArea);
  });
};

BusinessAreas.getAreas = (userId = Meteor.userId() ) => {
  areasFound = BusinessAreas.find({userId: userId});
  return areasFound
          ? areasFound
          : "";
};

BusinessAreas.attachSchema(businessAreasSchema);
