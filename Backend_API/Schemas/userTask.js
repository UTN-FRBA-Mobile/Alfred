import SimpleSchema from  'simpl-schema';
import { Mongo } from 'meteor/mongo';
import { Swots } from './swot';
import { Risks } from './risk';
import moment from 'moment';

export const UserTasks = new Mongo.Collection('userTasks');

const frequencySchema = new SimpleSchema({
  type: {
    type: String,
    label: "type",
    optional: true
  },
  value: {
    type: String,
    label: "value",
    optional: true
  },
  secondaryValue: {
    type: String,
    label: "value",
    optional: true
  },
  time: {
    type: String,
    label: "time",
    optional: true
  }
});

const taskSchema = new SimpleSchema({
  responsibleID: {
    type: String,
    label: "responsibleID",
    optional: true
  },
  supervisorID: {
    type: String,
    label: "supervisorID",
    optional: true
  },
  taskDescription: {
    type: String,
    label: "taskDescription"
  },
  frequency: {
    type: frequencySchema,
    label: "frequency"
  },
  status: {
    type: String,
    label: "status",
    optional:true
  },
  completed: {
    type: Boolean,
    label: "completed",
    optional:true
  },
  points: {
    type: Number,
    label: "points",
    optional: true
  }
});

const userTasksSchema = new SimpleSchema({
  userId: {
    type: String,
    label: "userId"
  },
  userEmail: {
    type: String,
    label: "userEmail"
  },
  businessArea: {
    type: String,
    label: "businessArea",
    defaultValue: 'all',
    optional: true
  },
  type: {
    type: String,
    label: "type"
  },
  subtype: {
    type: String,
    label: "subtype",
    optional: true
  },
  tasks: {
    type: Array,
    optional: true,
    label: "tasks"
  },
  'tasks.$': {
    type: taskSchema,
  },
  createdAt: {
    type: Date,
    autoValue: function() { if (!this.isSet) return new Date(); else return undefined; },
  }
});

UserTasks.insertPlanList = (plans, userId = Meteor.userId()  ) => {
  plans.forEach(plan => {
    plan.planTypeList.map(planType => {
      const businessArea = planType.data.planArea ? planType.data.planArea : 'all';
      const userTasks = {};
      UserTasks.remove({type: 'plan', subtype: plan.name, businessArea, userId: userId}) //FIXME need to put email also?
      userTasks.userId = userId;
      userTasks.userEmail = Meteor.users.findOne( userId ).emails[0].address;
      userTasks.type = 'plan';
      userTasks.subtype = plan.name;
      userTasks.businessArea = businessArea;
      userTasks.tasks = planType.data.planItems.map(planItem => ({
        responsibleID: planItem.data.responsible,
        supervisorID: planItem.data.supervisor,
        taskDescription: planItem.data.tool,
        frequency: {
          type: planItem.data.frequencyType,
          value: planItem.data.frequencyValue,
          secondaryValue: planItem.data.frequencySecondValue,
          time: planItem.data.frequency
        }
      }));
      UserTasks.insert(userTasks);
    });
  });
};

//Fixme: arreglar codigo repetido
UserTasks.insertSwotTasks = (swotTasks) => {
  UserTasks.remove({userId: Meteor.userId(), type: 'swot'});
  Swots.removeUserTaskIds();
  swotTasks.forEach(swotTask => {
    const newSwotTask = {
      responsibleID: swotTask.responsible,
      supervisorID: swotTask.supervisor,
      taskDescription: swotTask.tool,
      frequency: {
        type: swotTask.frequencyType,
        value: swotTask.frequencyValue,
        secondaryValue: swotTask.frequencySecondValue,
        time: swotTask.frequency
      }
    };
    const swotElement = Swots.findOne({userId: Meteor.userId(), description: swotTask.element});
    if (swotElement) {
      if (!swotElement.userTasksId) {
        const userTask = {};
        userTask.userId = Meteor.userId();
        userTask.type = 'swot';
        userTask.tasks = [newSwotTask];
        const newUserTaskId = UserTasks.insert(userTask);
        Swots.updateUserTaskId(swotElement._id, newUserTaskId);
      } else {
        UserTasks.update({_id: swotElement.userTasksId}, {$push: {tasks: newSwotTask}})
      }
    }
  });
}

UserTasks.insertContingencyPlans = (contingencyPlans) => {
  UserTasks.remove({userId: Meteor.userId(), type: 'contingencyPlan'});
  Risks.removeUserTaskIds();
  contingencyPlans.forEach(contingencyPlan => {
    const newContingencyPlanTask = {
      responsibleID: contingencyPlan.responsible,
      supervisorID: contingencyPlan.supervisor,
      taskDescription: contingencyPlan.tool,
      frequency: {
        type: contingencyPlan.frequencyType,
        value: contingencyPlan.frequencyValue,
        secondaryValue: contingencyPlan.frequencySecondValue,
        time: contingencyPlan.frequency
      }};
    const risk = Risks.findOne({userId: Meteor.userId(), risk: contingencyPlan.element});
    if (risk) {
      if (!risk.userTasksId) {
        const userTask = {};
        userTask.userId = Meteor.userId();
        userTask.type = 'contingencyPlan';
        userTask.tasks = [newContingencyPlanTask];
        const newUserTaskId = UserTasks.insert(userTask);
        Risks.updateUserTaskId(risk._id, newUserTaskId);
      } else {
        UserTasks.update({_id: risk.userTasksId}, {$push: {tasks: newContingencyPlanTask}})
      }
    }
  });
}

UserTasks.insertTaskIntoPlan = (planName, newTask) => {
  const userTasksFound = UserTasks.findOne({userId: Meteor.userId(), subtype: planName});
  if (userTasksFound) {
    UserTasks.update({userId: Meteor.userId(), subtype: planName}, {$push: {tasks: newTask}})
  } else {
    let newPlan = {
      userId: Meteor.userId(),
      userEmail: Meteor.users.findOne( Meteor.userId() ).emails[0].address,
      type: "plan",
      subtype: planName,
      businessArea: "all",
      tasks: [newTask]
    };
    UserTasks.insert(newPlan);
  }
  return UserTasks.findOne({userId: Meteor.userId(), subtype: planName});
}

UserTasks.updateReminders = (reminders) => {
  reminders.forEach(reminder => {
    const existingReminder = UserTasks.findOne({userId: reminder.entrepreneur, type: 'reminder'});
    const newReminderTask = {
      responsibleID: reminder.entrepreneur,
      taskDescription: reminder.businessName,
      frequency: {
        type: reminder.frequencyType,
        value: reminder.frequencyValue,
        secondaryValue: reminder.frequencySecondValue,
        time: reminder.frequency
      },
      status: reminder.status
    };
    if (!existingReminder) {
      const userTask = {};
      userTask.userId = reminder.entrepreneur;
      userTask.type = 'reminder';
      userTask.tasks = [newReminderTask];
      UserTasks.insert(userTask);
    } else {
      UserTasks.update({userId: reminder.entrepreneur, type: 'reminder'},
        {$set: {tasks: [newReminderTask]}});
    }
  });
}

UserTasks.obtainScheduledTasks = async () => {
  const queryDailyTasks = [
    { $unwind: "$tasks" }
    ,
    { $match: { 'tasks.frequency.type': { $eq: "dayAmount" }}}
  ];
  const queryWeeklyTasks = [
    { $unwind: "$tasks" }
    ,
    { $match: { 'tasks.frequency.type': { $eq: "weekAmount" }}}
  ];
  const queryMonthlyTasks = [
    { $unwind: "$tasks" }
    ,
    { $match: { 'tasks.frequency.type': { $eq: "monthAmount" }}}
  ];
  //console.log(`== DAY OF MONTH == ${moment().date()}`)
  const queryMonthlyDayTasks = [
    { $unwind: "$tasks" }
    ,
    { $match: { $and:[
        { 'tasks.frequency.type': { $eq: "monthDay" }}
        ,
        { 'tasks.frequency.value': { $eq: `${23}` }} //FIXME put moment().date() instead 23
    ]}}
  ];
  const queryNotReminderTasks = [
    { $unwind: "$tasks" }
    ,
    { $match: { 'tasks.frequency': {}}}
  ];

  function queryAggregate (queryToExecute) {
    return new Promise(function (resolve) {
      UserTasks.rawCollection().aggregate(
        queryToExecute,
        function(err, result) {
          // console.log("callback");
          if(err) {
            console.log("=== ERROR ===");
            console.log(err);
          }else{
            //console.log("=== Result ===");
            //console.log(result);
            resolve(result);
          }
        }
      );
    })
  }
  transformPlanName = { // To send with SendGrid
    "PLAN DE ADMINISTRACIÓN": "management_plan",
    "PLAN DE COMUNICACIÓN": "communication_plan",
    "PLAN COMERCIAL": "commercial_plan"
  };

  function transformTaskFormat (taskToTransform) {
    var tasksDictionary = {};

    taskToTransform.forEach(function(aTask) {
      // console.log("=== aTask ===");
      // console.log(aTask);
      const userEmail = aTask.userEmail;
      const plan = transformPlanName[aTask.subtype];
      const description = aTask.tasks.taskDescription;
      const responsible = aTask.tasks.responsibleID;
      const supervisor = aTask.tasks.supervisorID;
      const taskID = aTask._id;
      const user = Meteor.users.findOne({'_id': aTask.userId});
      // console.log("== USER FOUND ==");
      // console.log(user);

      if( !tasksDictionary[userEmail] ) 
        tasksDictionary[userEmail] = {}
        
      if( !tasksDictionary[userEmail]["name"] ) 
        tasksDictionary[userEmail]["name"] = user.personalInformation.name;
        
      if( !tasksDictionary[userEmail]["plans"] ) 
        tasksDictionary[userEmail]["plans"] = {}

      if( !tasksDictionary[userEmail]["plans"][plan] ) 
        tasksDictionary[userEmail]["plans"][plan] = []

      tasksDictionary[userEmail]["plans"][plan].push({
        "tool": description,
        "responsible": responsible,
        "supervisor": supervisor,
        "taskid": taskID
      });
    }, this);

    return tasksDictionary;
  }

  const daily_tasks = await queryAggregate(queryDailyTasks);
  // console.log("daily_tasks obtained:");
  // console.log(daily_tasks);
  let weekly_tasks = await queryAggregate(queryWeeklyTasks);
  // console.log("weekly_tasks obtained:");
  // console.log(weekly_tasks)
  let monthly_tasks = await queryAggregate(queryMonthlyTasks);
  // console.log("monthly_tasks obtained:");
  // console.log(monthly_tasks)
  let monthly_day_tasks = await queryAggregate(queryMonthlyDayTasks);
  // console.log("monthly_day_tasks obtained:");
  // console.log(monthly_day_tasks)
  let not_reminder_tasks = await queryAggregate(queryNotReminderTasks);
  // console.log("not_reminder_tasks obtained:");
  // console.log(not_reminder_tasks)

  const MONDAY = 1;
  if (moment().days() !== MONDAY){ 
    weekly_tasks = [];
  }
  if((moment().date() > 7) || (moment().days() !== MONDAY)){
    monthly_tasks = [];
    monthly_day_tasks = [];
  }

  const daily_tasks_filtered = daily_tasks.filter( aTask => {
    // console.log("== Current Day of Year ==");
    // console.log(moment().dayOfYear());
    //See if current day is a multiple of the defined day to send emails each 'frequency.value' days
    return ( moment().dayOfYear() % aTask.tasks.frequency.value) === 0
  });
  // console.log("daily_tasks_filtered:");
  // console.log(daily_tasks_filtered);

  const weekly_tasks_filtered = weekly_tasks.filter( aTask => {
    // console.log("== Current Week of Year ==");
    // console.log(moment().week());
    //See if current week is a multiple of the defined week to send emails each 'frequency.value' week
    return ( moment().week() % aTask.tasks.frequency.value) === 0
  });
  // console.log("weekly_tasks_filtered:");
  // console.log(weekly_tasks_filtered);
  const monthly_tasks_filtered = monthly_tasks.filter( aTask => {
    // console.log("== Current Month of Year ==");
    // console.log(moment().month());
    //See if current month is a multiple of the defined month to send emails each 'frequency.value' month
    return ( moment().month() % aTask.tasks.frequency.value) === 0
  });
  // console.log("monthly_tasks_filtered:");
  // console.log(monthly_tasks_filtered);
  const monthly_day_tasks_filtered = [...monthly_day_tasks];
  // console.log("monthly_day_tasks_filtered:");
  // console.log(monthly_day_tasks_filtered);


  const all_tasks_filtered = [...daily_tasks_filtered, ...weekly_tasks_filtered, ...monthly_tasks_filtered, ...monthly_day_tasks_filtered, ...not_reminder_tasks];
  // console.log("all_tasks_filtered: ");
  // console.log(all_tasks_filtered);
  return transformTaskFormat(all_tasks_filtered);
}

UserTasks.attachSchema(userTasksSchema);
