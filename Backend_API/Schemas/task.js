import { Mongo } from 'meteor/mongo';
import SimpleSchema from  'simpl-schema';

export const Tasks = new Mongo.Collection('tasks');

const tasksSchema = new SimpleSchema({
  text: {
    type: String,
    optional: false
  },
  owner: {
    type: String,
    optional: false
  },
  username: {
    type: String,
    optional: false
  },
  checked: {
    type: Boolean,
    optional: true
  },
  createdAt: {
    type: Date,
    autoValue: function() {
      if (!this.isSet) return new Date(); else return undefined;
    }
  }
});

Tasks.attachSchema(tasksSchema);
