import React, {useState} from 'react'
import { useSelector } from 'react-redux';
 import { Link , useHistory} from "react-router-dom";
import BodyHighlighter from './BodyHighlighter';
import PlanDayTable from './PlanDayTable';
import { addNewTrainingPlan } from '../../../services/TrainingPlanService';
import swal from "sweetalert";
import { Button } from "react-bootstrap";
import { store } from '../../../store/store';

export default function PlanDetailsStep() {

   const state = useSelector((state) => state.workoutwizard)
   const history = useHistory()

   const state2 = store.getState();
   let userId = state2.auth.auth.userId;

   const sendTrainingPlan = () => {
      let trainingPlan = {
         title: state.planName,
         description: state.additionalInformations,
         trainingDays: state.trainingDays,
         planPriority: state.planPriorirty,
         planType: state.planType,
         isPublic: state.isPublic,
         targetFinishDate: state.targetFinishDate,
         trainingUnitsDetails: state.exercises.map(e => {
            let uParts =  e.trainingUnitDetails.map((i,z) => {
               return {seriesNumber: z+1, numberOfRepetitions: i.numberOfRepetitions}
            })

            let uD = [{
               exerciseId: e.exerciseId,
               seriesRepetitionsDetails: uParts
            }]

            let tDetails = {
               trainingDay: e.trainingDay,
               trainingUnitParts: uD
            }
            return tDetails;
         }
        )
      }

      const filteredUnitDetails = trainingPlan.trainingUnitsDetails.filter(unit => {
         return trainingPlan.trainingDays.some(day => unit.trainingDay === day);
       });
       
       const missingDays = trainingPlan.trainingDays.filter(day => {
         return !filteredUnitDetails.some(unit => unit.trainingDay === day);
       });

       const result = filteredUnitDetails.concat(missingDays.map(day => ({trainingDay: day, fields: []})));
       trainingPlan = {...trainingPlan, trainingUnitsDetails: result}
       

      addNewTrainingPlan(trainingPlan, userId)
      .then(response => {
         if(response.status == 201){
            swal('Sukces', 'Plan treningowy został zapisany !', "success");
            setTimeout(navigateToHomePage, 3000)
         } else {
            swal('Sukces', 'Nie posiadasz zdefiniowanych dni treningowych. Wróć do poprzedniego kroku i uzupełnij szczegóły', "success");
         }
         
      })
      .catch(error =>{
         swal('Błąd', 'Wystąpił błąd w trakcie zapisu planu treningowego.', "error");
         console.log(error)})
}

const navigateToHomePage = () => {
   history.push('/dashboard');
 };
 

        return (
         <div>
            <div className="row">
               <div className="col-lg-12">
                  <div className="card">
                     <div className="card-header">
                        <h4 className="card-title">Wybierz ćwiczenia na każdą grupę mięsniową</h4>
                     </div>
                     <div className="card-body">
                            <BodyHighlighter />
                     </div>
                  </div>
               </div>
               <div className="col-lg-12">
                  <div className="card">
                     <div className="card-header">
                        <h4 className="card-title">Szkielet planu treningowe</h4>
                     </div>
                     <div className="card-body">
                       {(Array.isArray(state.trainingDays) && state.trainingDays.includes("MONDAY")) && <PlanDayTable trainingDay = {"MONDAY"} />} 
                       {(Array.isArray(state.trainingDays) && state.trainingDays.includes("TUESDAY")) && <PlanDayTable trainingDay = {"TUESDAY"} />} 
                       {(Array.isArray(state.trainingDays) && state.trainingDays.includes("WEDNESDAY")) && <PlanDayTable trainingDay = {"WEDNESDAY"} />} 
                       {(Array.isArray(state.trainingDays) && state.trainingDays.includes("THURSDAY")) && <PlanDayTable trainingDay = {"THURSDAY"} />} 
                       {(Array.isArray(state.trainingDays) && state.trainingDays.includes("FRIDAY")) && <PlanDayTable trainingDay = {"FRIDAY"} />} 
                       {(Array.isArray(state.trainingDays) && state.trainingDays.includes("SATURDAY")) && <PlanDayTable trainingDay = {"SATURDAY"} />} 
                       {(Array.isArray(state.trainingDays) && state.trainingDays.includes("SUNDAY")) && <PlanDayTable trainingDay = {"SUNDAY"} />} 
                     </div>
                  </div>
               </div>
            </div>
            <center> <Button className="m-sm-right mt-2 d-flex align-items-center justify-content-center btn" variant="primary" onClick = {() => sendTrainingPlan()} >
                               Zatwierdz plan treningowy i zapisz
                        </Button></center>
                     
            </div>
           );
}
