import React, {useState} from 'react'
import {
   Col,
   Card,
   Table,
   Button
} from "react-bootstrap";
import { Link } from "react-router-dom";
import { mapTrainingDay, mapMuscleGroup } from '../workout-create/CommonMappers';
import ExerciesEditModal from './ExerciseEditModal';

export default function PlanDayTable({trainingUnits, trainingDay, editable, exRemoveHandler, exUpdateHandler}) {
    
   const [counter, setCounter] = useState(1)

   const[showModal, setShowModal] = useState(false)
   const[editedUnitPart, setEditedUnitPart] = useState({})

   const disableModal = () => {
      setShowModal(false)
    }


    const handleRemoveExercise = e => {
         let trainingUnitPartId = e.id
         let trainingUnitId =  trainingUnits[0].id
         exRemoveHandler(trainingUnitId, trainingUnitPartId)
    }

    const handleEditExercise = e => {
      setEditedUnitPart(e)
      setShowModal(true)
    }

    const handleUpdateUnitPartDetails = e => {
      let trainingUnitId =  trainingUnits.find(unit => unit.trainingUnitParts.find(unitPart => unitPart.id === editedUnitPart.id)).id
      exUpdateHandler(e, editedUnitPart.id, trainingUnitId)
    }
    

    

 
   return (             
    <Col lg={12}>
     <Card>
    <Card.Header>
       <Card.Title>Dzień treningowy - {mapTrainingDay(trainingDay)}</Card.Title>
    </Card.Header>
    <Card.Body> 
    {/* //{setTrainingUnitPartDetails, currentUnitPartDetails, disableModal} */}
    {showModal && <ExerciesEditModal setTrainingUnitPartDetails={handleEditExercise} currentUnitPartDetails={editedUnitPart} disableModal={disableModal} handleSaveUnitPartDetails={handleUpdateUnitPartDetails}/>}
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
                 trainingUnits.map(unit => unit.trainingUnitParts
                .map(
                   (item, index) => {                 
                      return (
                         <tr key = {item.exerciseId}>
                            <td> {index+1} </td>
                            <td> {item.exerciseName} </td>
                            <td> {item.seriesRepetitionsDetails.length} </td>
                            <td> {item.seriesRepetitionsDetails.map((value, index) => {
                               if(index+1 === item.seriesRepetitionsDetails.length){
                                    return value.repetitionsNumber
                               }else {
                                  return value.repetitionsNumber+" - "
                               }
                            })} </td>

                             <td> {mapMuscleGroup(item.targetMuscle)} </td>
                            <td> {item.additionalMuscles.map((x) => mapMuscleGroup(x) + " , ")} </td> 
                                          <td>
                                             {editable && 
                   <div className="d-flex">
                      <Button
                         onClick={() => handleEditExercise(item)}
                         className="btn btn-primary shadow btn-xs sharp mr-1"
                      >
                         <i className="fa fa-pencil"></i>
                      </Button>
                      <Button
                         onClick={() => handleRemoveExercise(item)}
                         className="btn btn-danger shadow btn-xs sharp"
                      >
                         <i className="fa fa-trash"></i>
                      </Button>
                   </div> }
                </td>
                         </tr>
                         
                      )
             
                   }
                ))
            } 
          </tbody>
       </Table>
    </Card.Body>
 </Card>
 </Col>
   )
 }
 