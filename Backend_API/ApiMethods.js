import { Meteor } from 'meteor/meteor';
import { Favourites } from '../../../../lib/schemas/favourites';

if (Meteor.isServer) {
  Meteor.methods({
    /*
    * REQUIRES: 
    * email
    * password
    * name
    */
    'api.RegisterUser'(data) {
      console.log("Called api.RegisterUser");
      
      try {
        console.log("=== REGISTRATION ===");
        console.log(`${data.email} is trying to register`);

        if (
          !data.email || data.email === '' ||
          !data.password || data.password === ''
        ) {
          throw new Meteor.Error('Incomplete fields');
        }
        const existingUser = Meteor.users.findOne({'emails.address': data.email});
        if (existingUser) {
          throw new Meteor.Error('User already exists');
        }
        const EMAIL_SEPARATOR = '@';
        const FIRST = 0;
        const extracted_name = data.email.split(EMAIL_SEPARATOR)[FIRST].split('.')[FIRST];
        const capitalizedName = capitalize(extracted_name);
        const newUserId = Accounts.createUser({
          email: data.email,
          password: data.password,
          name: capitalizedName,
          surname: capitalizedName
        });
        // Send Emails
        const TEMPLATE_BASICO_REGISTRO_USUARIO = Meteor.settings.private.TEMPLATE_BASICO_REGISTRO_USUARIO;
        const user_email_data = {
          "name" : capitalizedName,
        };
        Meteor.call('sendgrid.sendEmail', TEMPLATE_BASICO_REGISTRO_USUARIO, data.email, user_email_data, (error, response) => {
          console.log("Send Registration Email");
          if(error) {
              console.error("==== ERROR ===");
              console.error(error);
              console.error("==== TRACE ===");
              console.trace();
          }
        });

        const TEMPLATE_BASICO_REGISTRO_NOSOTROS = Meteor.settings.private.TEMPLATE_BASICO_REGISTRO_NOSOTROS;
        const SENDGRID_FROM_EMAIL = Meteor.settings.private.SENDGRID_FROM_EMAIL;
        const alert_user_registered_data = {
          "name" : extracted_name,
          "email": data.email,
        };
        Meteor.call('sendgrid.sendEmail', TEMPLATE_BASICO_REGISTRO_NOSOTROS, SENDGRID_FROM_EMAIL, alert_user_registered_data, (error, response) => {
          console.log("Send Registration Alert to Us");
          if(error) {
              console.error("==== ERROR ===");
              console.error(error);
              console.error("==== TRACE ===");
              console.trace();
          }
        });
        
        return newUserId;
      } catch (exception) {
        validationsHelper.parseMongoError(exception);
        return exception;
      }
    },

  });
}
