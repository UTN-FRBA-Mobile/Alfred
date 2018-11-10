import { Meteor } from 'meteor/meteor';
import { Favourites } from '../../../../lib/schemas/favourites';

if (Meteor.isServer) {
  Meteor.methods({
    'insertFavourite'(aFav) {
      console.log("Started insertFavourite");
      try {
        const newFavourite = Favourites.insertFavourite(aFav);
        return newFavourite;
      } catch (exception) {
        console.error("=== ERROR on insertFavourite ===");
        console.error(exception);
        console.trace();
        throw exception;
      }
    },
    'favourites.get'() {
      console.log("Started insertFavourite");
      try {
        const aFavourite = Favourites.get();
        return aFavourite;
      } catch (exception) {
        console.error("=== ERROR on favourites.get ===");
        console.error(exception);
        console.trace();
        throw exception;
      }
    },
    'favourites.test'(data) {
      console.log("Called favourites.test Endpoint");
      console.log(data);
      console.log(data.value);
      console.log(data.value.adentro);
      return {
        prueba: "valor string",
        valor: 3,
        objeto: {
          adentro: true
        }
      };
    },
  });
}
