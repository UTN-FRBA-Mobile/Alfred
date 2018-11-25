import SimpleSchema from  'simpl-schema';
import { Mongo } from 'meteor/mongo';

export const Favourites = new Mongo.Collection('favourites');

const favsSchema = new SimpleSchema({
  title: {
    type: String
  },
  description: {
    type: String,
    optional: true
  },
  link: {
    type: String,
  },
  imageUrl: {
    type: String,
    optional: true
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

Favourites.insertFavourite = (aFav, userId = Meteor.userId()  ) => {
  const favouriteFound = Favourites.findOne({userId: userId});
    if (favouriteFound) {
      Favourites.update({_id: favouriteFound._id}, {$push: {favs: aFav}});
      return favouriteFound;
    } else {
      const newFavourite = {
        favs: [aFav]
      };
      newFavourite.userId = userId;
      Favourites.insert(newFavourite);
      return Favourites.findOne({userId: userId});
    }
};

Favourites.deleteFavourite = (link, userId = Meteor.userId()  ) => {
  const favouriteFound = Favourites.findOne({userId: userId});
    if (favouriteFound) {
      let updatedFavs = favouriteFound.favs.filter(
        aFav => aFav.link !== link
        )
      Favourites.update({_id: favouriteFound._id}, {$set: {favs: updatedFavs}});
    }
};

Favourites.get = (userId = Meteor.userId() ) => {
  favouriteFound = Favourites.findOne({userId: userId});
  return favouriteFound
          ? favouriteFound
          : [];
};

Favourites.attachSchema(favouritesSchema);
