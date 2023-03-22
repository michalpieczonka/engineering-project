import React, { useState } from 'react'
/// React router dom
import {Switch, Route } from 'react-router-dom'
/// Css
import './index.css'
import './chart.css'
import './step.css'

import Nav from './layouts/nav'
import Footer from './layouts/Footer'

import Home from "./components/Dashboard/Home";
import WorkoutStatistic from "./components/Dashboard/WorkoutStatistic";
import WorkoutPlan from "./components/Dashboard/WorkoutPlan";
import CreateWorkoutWizard from './components/workout-create/CreateWorkoutPlanWizard'
import PersonalTrainingPlans from './components/Dashboard/PersonalTrainingPlans'
import PublicTrainingPlans from './components/training-plans/public-plans/PublicTrainingPlans'

import Registration from './pages/Registration'
import Login from './pages/Login'


import TrainingPlanDetails from './components/training-plans/TrainingPlanDetails'
import WorkoutPerformTemplate from './components/workout/WorkoutPerformTemplate'
import WorkoutPerformedTemplate from './components/workout/WorkoutPerformedTemplate'
import UserProfile from './components/profile-details/UserProfile'
import PublicPlanDetails from './components/training-plans/public-plans/PublicPlanDetails'

const Markup = () => {
  let path = window.location.pathname
  path = path.split('/')
  path = path[path.length - 1]
  let pagePath = path.split('-').includes('page')
  const [activeEvent, setActiveEvent] = useState(!path)

  const routes = [
    { url: '', component: Home },
    { url: 'dashboard', component: Home },

    //workouts
	  { url: "workout-statistic", component: WorkoutStatistic },
    { url: "workout-center", component: WorkoutPlan },
    { url: "create-workout-wizard", component: CreateWorkoutWizard },
    {url: "workouts/:workoutId", component: WorkoutPerformedTemplate },

    //training plans
    {url : "training-plan/:id", component: TrainingPlanDetails },
    {url: "training-plan/:trainingPlanId/perform/:trainingUnitId", component: WorkoutPerformTemplate },
    {url: "training-plans", component: PersonalTrainingPlans },


    //community
    {url: "community/training-plans", component: PublicTrainingPlans },
    {url : "community/training-plan/:id", component: PublicPlanDetails },

    /// profile
    { url: 'profile', component: UserProfile },



    /// login/register
    { url: 'page-register', component: Registration },
    { url: 'page-login', component: Login },
  ]

  return (
       <> 
          <div
            id={`${!pagePath ? 'main-wrapper' : ''}`}
            className={`${!pagePath ? 'show' : 'mh100vh'}`}
          >
            {!pagePath && (
              <Nav
                onClick={() => setActiveEvent(!activeEvent)}
                activeEvent={activeEvent}
                onClick2={() => setActiveEvent(false)}
                onClick3={() => setActiveEvent(true)}
              />
            )}
            <div
              className={` ${!path && activeEvent ? 'rightside-event' : ''} ${
                !pagePath ? 'content-body' : ''
              }`}
            >
              <div
                className={`${!pagePath ? 'container-fluid' : ''}`}
                style={{ minHeight: window.screen.height - 60 }}
              >
                <Switch>
                  {routes.map((data, i) => (
                    <Route
                      key={i}
                      exact
                      path={`/${data.url}`}
                      component={data.component}
                    />
                  ))}
                </Switch>
              </div>
            </div>
            {!pagePath && <Footer />}
          </div>
       </>
  )
}

export default Markup
