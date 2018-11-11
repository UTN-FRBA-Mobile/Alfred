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
        prueba: "El Endpoint de la API fue llamado exitosamente, abajo puede verificar sus datos",
        datos_originales_que_llegaron: data,
        value: data.value,
        adentro: {
          adentro: data.value.adentro
        }
      };
    },
  });
}
