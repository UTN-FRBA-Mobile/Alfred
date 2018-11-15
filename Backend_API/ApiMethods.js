import { Meteor } from 'meteor/meteor';
import { Mvps } from '../../../../lib/schemas/mvp';
import { BusinessAreas } from '../../../../lib/schemas/businessArea';
import { Favourites } from '../../../../lib/schemas/favourites';

if (Meteor.isServer) {
  Meteor.methods({
    /*
    * REQUIRES: 
    * email
    * password
    * name
    * RETURNS
    * success (Boolean)
    * error or userId
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
        
        return {
          success: true,
          newUserId: newUserId
        };
      } catch (exception) {
        validationsHelper.parseMongoError(exception);
        return {
          success: false,
          error: exception
        };
      }
    },

    /*
    * REQUIRES: 
    * userId
    */
    'api.query'(data) {
      console.log("=== Calling api.query ===");
      const mvpLast = Mvps.findOne({userId: data.userId})
      console.log("MVP Found");
      console.log(JSON.stringify(mvpLast));
      if (mvpLast == undefined){
        return "aprender emprender emprendimiento argentina";
      }
      let concatResult = mvpLast && mvpLast.name;
      concatResult = mvpLast && mvpLast.description && mvpLast.description.concat(` ${concatResult}`) || concatResult;
      
      const canvasLast = BusinessAreas.findOne({userId: data.userId})
      console.log("CANVAS Found");
      console.log(JSON.stringify(canvasLast));
      concatResult = canvasLast && canvasLast.name && canvasLast.name.concat(` ${concatResult}`) || concatResult;
      concatResult = canvasLast && canvasLast.partners && canvasLast.partners.concat(` ${concatResult}`) || concatResult;
      concatResult = canvasLast && canvasLast.partners && canvasLast.name && canvasLast.partners.concat(` ${canvasLast.name}`) || concatResult;
      concatResult = canvasLast && canvasLast.valueProposition && canvasLast.valueProposition.concat(` ${concatResult}`) || concatResult;
      concatResult = canvasLast && canvasLast.valueProposition && canvasLast.name && canvasLast.valueProposition.concat(` ${canvasLast.name}`) || concatResult;

      console.log(`Concat Result: ${concatResult}`);
      const finalQuery = concatResult.replace(/ /gm, '+');
      console.log(`Final Query: ${finalQuery}`);
      return finalQuery;
    },
    /*
    * REQUIRES: 
    * email
    * password
    * RETURNS:
    * success (Boolean)
    * error or userData
    */
    'api.login'(data) {
      console.log("=== Calling api.login ===");
      //TODO password must be Bcripted in App? Or at least use SHA256
      Meteor.loginWithPassword(data.email, data.password, (err) => {
        if (err) {
          validationsHelper.displayServerError(err);
          return {
            success: false,
            error: TAPi18n.__('error.login')
          };
        } else {
          const existingUser = Meteor.users.findOne({'emails.address': data.email});
          return {
            success: true,
            userData: existingUser
          };
        }
      });
    },
    /*
    * REQUIRES: 
    * userId
    * selectedAnswers (array)
    * questionNumber (Integer)
    * hasEndedInterview (Boolean)
    * RETURNS:
    * success (Boolean)
    * error or userId
    */
    'api.saveTraits'(data) {
      console.log("=== Calling api.saveTraits ===");
      const userID = data.userId;
      const selectedAnswers = data.selectedAnswers;
      const questionNumber = data.questionNumber;
      const hasEndedInterview = data.hasEndedInterview;
      try {
        const userToUpdate = Meteor.users.findOne({ _id: userID });

        if (!userToUpdate) {
          throw new Meteor.Error('user-not-found');
        }

        console.log("inside saveTraits");

        Meteor.users.update({_id: userID}, {$set: {'personalInformation.currentQuestionNumber': questionNumber}});

        Meteor.users.update({_id: userID}, {$addToSet: { goals: { $each: selectedAnswers.goals }}});
        Meteor.users.update({_id: userID}, {$addToSet: { contributions: { $each: selectedAnswers.contributions }}});
        Meteor.users.update({_id: userID}, {$addToSet: { identity_traits: { $each: selectedAnswers.identity_traits }}});
        Meteor.users.update({_id: userID}, {$addToSet: { perpetual_identity: { $each: selectedAnswers.perpetual_identity }}});
        
        if (hasEndedInterview && Roles.userIsInRole(Meteor.userId(), ['entrepreneur']) &&
          Meteor.user() && Meteor.user().personalInformation.status === 'pendingChatbot') {
          Meteor.users.update({_id: userID}, {$set: {'personalInformation.status': 'pendingPlans'}});
        }

        console.log('traits saved');
        return {
          success: true,
          userId: userID
        }
      } catch (exception) {
        console.log(exception);
        return {
          success: false,
          error: exception
        }
      }
    },
    /*
    * REQUIRES: 
    * userId
    * favourite (JSON with title, description and link)
    * RETURNS:
    * success (Boolean)
    * error or favorites (array)
    */
    'api.insertFavourite'(data) {
      console.log("=== Calling api.insertFavourite ===");
      try {
        const allFavorites = Favourites.insertFavourite(data.favourite, data.userId);
        return {
          success: true,
          favorites: allFavorites
        };
      } catch (exception) {
        console.error("=== ERROR on api.insertFavourite ===");
        console.error(exception);
        console.trace();
        return {
          success: false,
          error: exception
        };
      }
    },
  });
}
