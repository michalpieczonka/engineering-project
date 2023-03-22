import React, {useState} from 'react'
import { bindActionCreators } from 'redux';
import { useDispatch, useSelector } from 'react-redux';
import {
   Col,
   Card,
   Table,
} from "react-bootstrap";
import { Link } from "react-router-dom";
import { mapTrainingDay, mapMuscleGroup } from './CommonMappers';
import { removeExercise } from '../../../store/actions/WorkoutWizardActions';


export default function PlanDayTable({trainingDay}) {
   const state = useSelector((state) => state.workoutwizard)

   const [counter, setCounter] = useState(1)

   const dispatch = useDispatch();
   bindActionCreators(    { removeExercise },    dispatch)

   const handleRemoveExercise = e => {
      dispatch(removeExercise(e))
   }

  return (             
   <Col lg={12}>
    <Card>
   <Card.Header>
      <Card.Title>Dzień treningowy - {mapTrainingDay(trainingDay)}</Card.Title>
   </Card.Header>
   <Card.Body>
      <Table responsive>
         <thead>
            <tr>
               <th>
                  <strong>Numer ćwiczenia</strong>
               </th>
               <th>
                  <strong>Nazwa ćwiczenia</strong>
               </th>
               <th>
                  <strong>Liczba serii</strong>
               </th>
               <th>
                  <strong>Liczba powtórzeń</strong>
               </th>
               <th>
                  <strong>Docelowa grupa mięśniowa</strong>
               </th>
               <th>
                  <strong>Dodatkowe grupy mięsniowe</strong>
               </th>
            </tr>
         </thead>
         <tbody>
           {
               state.exercises.filter((x) => x.trainingDay === trainingDay)
               .map(
                  item => {
                     
                     return (
                        <tr key = {item.exerciseId}>
                           <td> {counter} </td>
                           <td> {item.exerciseName} </td>
                           <td> {item.trainingUnitDetails.length} </td>
                           <td> {item.trainingUnitDetails.map((value, index) => {
                              if(index+1 === item.trainingUnitDetails.length){
                                   return value.numberOfRepetitions
                              }else {
                                 return value.numberOfRepetitions+" - "
                              }
                           })} </td>
                           <td> {mapMuscleGroup(item.exerciseTargetMuscle)} </td>
                           <td> {item.exerciseAdditionalMuscles.map((x) => mapMuscleGroup(x) + " , ")} </td>
                                         <td>
                  <div className="d-flex">

                     <Link
                        onClick={() => handleRemoveExercise(item)}
                        className="btn btn-danger shadow btn-xs sharp"
                     >
                        <i className="fa fa-trash"></i>
                     </Link>
                  </div>
               </td>
                        </tr>
                     )
                  
                  }
               )
           } 
         </tbody>
      </Table>
   </Card.Body>
</Card>
</Col>
  )
}
