import React, {useEffect, useState} from 'react'
import { Button, Modal, Tab, Tabs } from "react-bootstrap";
import { Link , useHistory, useParams} from "react-router-dom";
import Select from "react-select";
import swal from "sweetalert";
import { mapTrainingDay, mapMuscleGroup } from '../workout-create/CommonMappers';

export default function WorkoutUnitSelectorModal() {

   //  const state = store.getState();
   //  let userId = state.auth.auth.userId;

  const history = useHistory()
  const [exercises, setExercises] = useState('')
  const [largeModal, setLargeModal] = useState(true);
  const [selectedExercise, setSelectedExercise] = useState('')
  const [selectedTrainingDay, setSelectedTrainingDay] = useState('')
  const [trainingUnitDetails, setTrainingUnitDetails] = useState([])


const [refresh, setRefresh] = useState(1)

// useEffect(() => { 
//     getTrainingPlan(trainingPlanId).then((response) => {       
//         let plan = response.data
//         let tmp = {
//             id: plan.id,
//             title:  plan.title,
//             description: plan.planDescription,
//             numberOfTrainingDays: plan.numberOfTrainingDays,
//             trainingDays: assignDefaultTrainingDays(plan.trainingDays),
//             planPriority: plan.planPriority,
//             planType: 'HYBRID', //todo: fix it
//             preferredTrainingIntership: plan.preferredTrainingIntership,
//             planCreatorUserId: plan.planCreatorUserId,
//             planUsersIds: plan.planUsersIds,
//             isPublic: plan.public,
//             planRates: plan.planRates.map(x => ({id: x.id, description: x.description, rate: x.rate})),
//             trainingUnits: plan.trainingUnits.map(unit => ({id: unit.id, trainingDay: unit.trainingDay, trainingUnitParts:
//                 unit.trainingUnitParts.map(part => ({id: part.id, exerciseId: part.exerciseId, exerciseName: part.exerciseName, targetMuscle: part.targetMuscle,
//                  additionalMuscles: part.additionalMuscles, //.map(m => mapMuscleGroup(m)), 
//             seriesRepetitionsDetails: part.seriesRepetitionsDetails.map(s => ({seriesNumber: s.seriesNumber, repetitionsNumber: s.repetitionsNumber}))}))}))
//         }

//         setTrainingPlan(tmp)
//         setIsLoading(false)
//     }).catch(error => swal("Błąd", "Nie udało się załadować szczegółów planu treningowego", "error").then(() => history.push('/training-plans')))
//    }, [refresh]);
  

  
  const trainingDaysOptions = [{value: 'asd', label: 'wtorek'}]//planDetails.trainingDays.map(it => ({value: it, label: mapTrainingDay(it)}))

  const handleAddExercise = () => {
      let exercise = {
         exerciseId: selectedExercise.id,
         exerciseName: selectedExercise.name,
         exerciseTargetMuscle: selectedExercise.targetMuscle,
         exerciseAdditionalMuscles: selectedExercise.additionalMuscles,
         trainingUnitDetails: trainingUnitDetails,
         trainingDay: selectedTrainingDay.value
      }
      dispatch(addExercise(exercise))
      showAlert(true, 'Ćwiczenie zostało dodane do planu treningowego !')
      disableModal()
   }

  const handleAddNewExercise = () => {
         addNewExercise(newExercise)
         .then(response => {
            showAlert(true, 'Ćwiczenie zostało dodane do bazy ćwiczeń. Dziękujemy !')
            clearNewExercise()
         })
         .catch(error => showAlert(false,'Nie udało się dodać ćwiczenia. Wystąpił błąd' ))
   }

   const showAlert = (success, message) => {
         if(success){
            swal('Sukces', message, "success");
         }
         else {
            swal('Oops', message, "error");
         }
   }
  
  return (
    <Modal
    className="fade bd-example-modal-lg"
    show={largeModal}
    size="lg"
 >
   
    <Modal.Header>
       <Modal.Title>Nowe ćwiczenie</Modal.Title>
       <Button
          variant=""
          className="close"
          onClick={disableModal}
       >
          <span>&times;</span>
       </Button>
    </Modal.Header>
    <Modal.Body>

     
      <div className="card-body">
      <h4 class="mm-b-0">Wybierz dzień treningowy aby rozpocząć trening</h4>
         <span>Aktualnie wykonywany plan: <b>asd</b> </span> <br /> 
         <br />
         <br />
                     <div className="basic-form">
                        <div className="form-group row">
                        <label className="col-lg-4 col-form-label">Plan treningowy</label>
                           <div className="col-lg-6">
                           <Select
                              defaultValue={selectedExercise}
                              onChange={(e) => setSelectedExercise(e)}
                              options={exercises}
                              getOptionLabel={(option) => option.name}
                              getOptionValue={(option) => option.id}
                                    style={{
                                    lineHeight: "40px",
                                    color: "#7e7e7e",
                                    paddingLeft: " 15px",
                            }} />
                           </div>
                           </div>


                           <div className="form-group row">
                        <label className="col-lg-4 col-form-label">Dzień treningowy</label>
                           <div className="col-lg-6">
                           <Select
                             value={selectedTrainingDay}
                              onChange={(e) => setSelectedTrainingDay(e)}
                              options={trainingDaysOptions}
                               getOptionValue={(option) => option.value}
                                    style={{
                                    lineHeight: "40px",
                                    color: "#7e7e7e",
                                    paddingLeft: " 15px",
                            }} />
                           </div>
                           </div>
      

  

               
                     </div>

                  </div>
                  <center>
                     <button  className="btn btn-primary" onClick={() => handleAddExercise()}>
                        Rozpocznij trening
                         </button>
                  </center>   

      </Modal.Body>
    <Modal.Footer>
       <Button
          variant="danger light"
          onClick={disableModal}
       >
          Zamknij
       </Button>
    </Modal.Footer>
 </Modal>
  )
}
