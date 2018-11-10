import { Meteor } from 'meteor/meteor';
import { Favourites } from '../../../../lib/schemas/favourites';

if (Meteor.isServer) {
  Meteor.methods({
    'api.RegisterUser'(data) {
      console.log("Called api.RegisterUser");
      
      return true;
    },
  });
}
