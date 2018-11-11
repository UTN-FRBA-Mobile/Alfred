import SimpleSchema from  'simpl-schema';
import { Mongo } from 'meteor/mongo';

export const Favourites = new Mongo.Collection('favourites');

const favsSchema = new SimpleSchema({
  title: {
    type: String
  },
  description: {
    type: String
  },
  link: {
    type: String
  }
});

const favouritesSchema = new SimpleSchema({
  userId: {
    type: String,
    label: "userId"
  },
  favs: {
    type: Array
  },
  'favs.$': {
    type: favsSchema
  },
  createdAt: {
    type: Date,
    autoValue: function() { if (!this.isSet) return new Date(); else return undefined; },
  }
});

Favourites.insertFavourite = (aFav) => {
  const favouriteFound = Favourites.findOne({userId: Meteor.userId()});
    if (favouriteFound) {
      Favourites.update({_id: favouriteFound._id}, {$push: {favs: aFav}});
      return favouriteFound;
    } else {
      const newFavourite = {
        favs: [aFav]
      };
      newFavourite.userId = Meteor.userId();
      Favourites.insert(newFavourite);
      return Favourites.findOne({userId: Meteor.userId()});
    }
};

Favourites.get = () => {
  favouriteFound = Favourites.findOne({userId: Meteor.userId()});
  return favouriteFound
          ? favouriteFound
          : [];
};

Favourites.attachSchema(favouritesSchema);
