import React, {useEffect, useState} from 'react'
import { Button, Modal, Tab, Tabs } from "react-bootstrap";
import Select from "react-select";
import swal from "sweetalert";

import {
   getAllExercisesByTargetMuscle,
   addNewExercise
} from '../../../services/ExerciseService';
import { mapMuscleGroup } from '../workout-create/CommonMappers';


export default function EditExerciesPickerModal({muscleGroup, disableModal, trainingDays, addExerciseHandler}) {

  const [exercises, setExercises] = useState('')
  const [largeModal, setLargeModal] = useState(true);
  const [selectedExercise, setSelectedExercise] = useState('')
  const [selectedTrainingDay, setSelectedTrainingDay] = useState('')
  const [numberOfSeries, setNumberOfSeries] = useState('')
  const [trainingUnitDetails, setTrainingUnitDetails] = useState([])

   const handleNumberOfSeries = (numberOfSeries, index, event) => {
      if (numberOfSeries > 10){
         swal('Oops', 'Liczba serii wieksza od 10 jest niedozwolona.', "error");
         setTrainingUnitDetails([])
      } else {
         if(numberOfSeries !== 0 && index === -1 && event === -1){
            setTrainingUnitDetails([])       
            setNumberOfSeries(numberOfSeries);
            var tmpUnitDetails = [];
            for(let i=0; i<numberOfSeries; i++){
               tmpUnitDetails.push( { seriesNumber: i+1, repetitionsNumber: '' })
            }
            setTrainingUnitDetails(tmpUnitDetails)
   
         } 
         else if(numberOfSeries === -1){
            let data = [...trainingUnitDetails];
            let updatedDetails = data.map(x => {
               if(x.seriesNumber === index+1)
               {                
                  return {...x,  repetitionsNumber: event.target.value}
               } else{
                  return x
               }
            })
            setTrainingUnitDetails(updatedDetails);
         } 
      }

   }

  const muscles = [
   {value: "TRAPEZIUS", label: "Czworoboczny"},
   {value: "UPPER_BACK", label: "Najszerszy grzbietu"},
   {value: "LOWER_BACK", label: "Prostowniki"},
   {value: "BACK_DELTOIDS", label: "Naramienny tylny"},
   {value: "TRICEPS", label: "Triceps"},
   {value: "FOREARM", label: "Przedramie"},
   {value: "GLUTEAL", label: "Pośladek"},
   {value: "HAMSTRING", label: "Mięsień prosty uda"},
   {value: "ADDUCTOR", label: "Przywodziciel"},
   {value: "CALVES", label: "Łydki"},
   {value: "QUADRICEPS", label: "Czworogłowy uda"},
   {value: "ABDUCTORS", label: "Odwodziciel"},
   {value: "ABS", label: "Brzuch"},
   {value: "OBLIQUES", label: "Mięśnie skośne brzucha"},
   {value: "CHEST", label: "Klatka piersiowa"},
   {value: "FRONT_DELTOIDS", label: "Naramienny przedni"},
   {value: "BICEPS", label: "Biceps"}
];

const exerciseTypes = [
   {value: "ISOLATED", label: "Izolowane"},
   {value: "COMPOUNDED", label: "Złożone"}
]

const getSelectedMuscle =  () => {
   try{
      var t = muscles.findIndex((x) => x.value === muscleGroup.replace('-', '_').toUpperCase())
      return t;
   } catch (err) {console.log(err)}

}

const[newExercise, setNewExercise] = useState({
   name: '',
   description: '',
   videoUrl: '',
   exerciseType: '',
   targetMuscle: muscles[getSelectedMuscle()].value,
   additionalMuscles: []
})

const [refresh, setRefresh] = useState(1)

const [newExerciseAdditionalMuscles, setNewExerciseAdditionalMuscles] = useState('')

const handleChangeNewExerciseAdditionalMuscles = (e) => {
   setNewExerciseAdditionalMuscles(prevState => ({
             ...prevState,
             exercises: Array.isArray(e) ? e.map(x => x.value) : []
       }))

       setNewExercise((prevState => ({
         ...prevState,
         additionalMuscles: Array.isArray(e) ? e.map(x => x.value) : []
   })))
}
  

  useEffect(() => { 
   getAllExercisesByTargetMuscle(muscleGroup).then((response) => {
      let tmp = []
      const allExercises = response.data.map(x => tmp.push({id: x.exerciseId, name: x.exerciseName, targetMuscle: x.targetMuscle, additionalMuscles: x.additionalMuscles}))
      console.log(tmp)
      setExercises(tmp)
   }).catch(error => console.error('Error: '+error))
  }, [refresh]);
  

  const trainingDaysOptions = trainingDays
  console.log(trainingDaysOptions)

  const handleAddExercise = () => {
    let trainingDay = selectedTrainingDay.value
      let exercise = {
         exerciseId: selectedExercise.id,
         exerciseName: selectedExercise.name,
         targetMuscle: selectedExercise.targetMuscle,
         additionalMuscles: selectedExercise.additionalMuscles,
         seriesRepetitionsDetails: trainingUnitDetails       
      }
      swal({
        title: "Dodawnie nowego ćwiczenia do planu treningowego",
        text: "Czy napewno chcesz dodać nowe ćwiczenie  do planu treningowego ?",
        icon: "warning",
        buttons: [
          'Nie, anuluj',
          'Tak, dodaj'
        ],
        dangerMode: true,
      }).then(function(isConfirm) {
        if (isConfirm) {
            addExerciseHandler(exercise, trainingDay)
            disableModal()
        } 
      });
      
   }

  const handleAddNewExercise = () => {
         addNewExercise(newExercise)
         .then(response => {
            showAlert(true, 'Ćwiczenie zostało dodane do bazy ćwiczeń. Dziękujemy !')
            clearNewExercise()
         })
         .catch(error => showAlert(false,'Nie udało się dodać ćwiczenia. Wystąpił błąd' ))
   }

   const clearNewExercise = () => {
      setNewExerciseAdditionalMuscles('')
      setNewExercise({
         name: '',
         description: '',
         videoUrl: '',
         exerciseType: '',
         targetMuscle: muscles[getSelectedMuscle()].value,
         additionalMuscles: []
      })
      setRefresh((prevState) => prevState+1)
   }

   const setAvailableMuscles = () => {
      return muscles.filter(x => x.value != muscleGroup.replace('-', '_').toUpperCase())
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
      <Tabs
      defaultActiveKey="addToPlan"
      transition={false}
      id="noanim-tab-example"
      className="mb-3"
      >
      <Tab eventKey="addToPlan" title="Dodaj ćwiczenie do planu treningowego">
      <div className="card-body">
      <h4 class="mm-b-0">Dodawanie nowego ćwiczenia</h4>
         <span>Wybrana grupa mięsniowa: <b>{mapMuscleGroup(muscleGroup.toUpperCase())}</b></span>
         <br />
         <br />
                     <div className="basic-form">
                        <div className="form-group row">
                        <label className="col-lg-4 col-form-label">Ćwiczenie</label>
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
      

                        <div className="form-group row">
                                    <label
                                       className="col-lg-4 col-form-label"
                                       htmlFor="val-digits"
                                    >
                                       Liczba serii{" "}
                                    </label>
                                    <div className="col-lg-6">
                                       <input
                                          onChange={(e) => handleNumberOfSeries(e.target.value, -1, -1)}
                                          type="text"
                                          className="form-control"
                                          id="val-digits"
                                          name="val-digits"
                                       />
                                    </div>
                                 </div>

                                    {trainingUnitDetails.length > 0 && trainingUnitDetails.map((input, index) => {
                                       return (                                                                              
                                 <div  key={index} className="form-group row">                          
                                             <label
                                       className="col-lg-4 col-form-label"
                                       htmlFor="val-digits"
                                    >
                                       Powtórzenia [Seria {index+1}]
                                    </label>
                                    <div className="col-lg-6">
                                          <input
                                            type="text"
                                             name='repetitions'
                                             placeholder='Liczba powtórzeń'
                                            // value={input.numberOfRepetitions}
                                             onChange={event => handleNumberOfSeries(-1, index, event)}
                                             className="form-control"
                                          />
                                          </div>
                                          </div>                                
                                       )
                                    })}
               
                     </div>

                  </div>
                  <center>
                     <button  className="btn btn-primary" onClick={() => handleAddExercise()}>
                        Dodaj ćwiczenie do planu treningowego
                         </button>
                  </center>   
      </Tab>
      <Tab eventKey="newExercise" title="Dodaj nowe ćwiczenie do bazy ćwiczeń">
      <div className="card-body">
                     <div className="basic-form">
                        <div className="form-group row">
                                    <label
                                       className="col-lg-4 col-form-label"
                                    >
                                       Nazwa ćwiczenia
                                    </label>
                                    <div className="col-lg-6">
                                       <input
                                          value = {newExercise.name}
                                          onChange={(e) => setNewExercise(
                                             (prevState) => ({
                                                ...prevState,
                                                name: e.target.value
                                          }))}
                                          type="text"
                                          className="form-control"
                                       />
                                    </div>
                                 </div>

                                 <div className="form-group row">
                                    <label
                                       className="col-lg-4 col-form-label"
                                    >
                                       Opis ćwiczenia
                                    </label>
                                    <div className="col-lg-6">
                                       <input
                                       value={newExercise.description}
                                          onChange={(e) => setNewExercise(
                                             (prevState) => ({
                                                ...prevState,
                                                description: e.target.value
                                          }))}
                                          type="text"
                                          className="form-control"
                                       />
                                    </div>
                                 </div>    

                                 <div className="form-group row">
                                    <label
                                       className="col-lg-4 col-form-label"
                                    >
                                       Adres filmu instruktażowego
                                    </label>
                                    <div className="col-lg-6">
                                       <input
                                       value={newExercise.videoUrl}
                                          onChange={(e) => setNewExercise(
                                             (prevState) => ({
                                                ...prevState,
                                                videoUrl: e.target.value
                                          }))}
                                          type="text"
                                          className="form-control"
                                       />
                                    </div>
                                 </div>   

                                 
                        <div className="form-group row">
                        <label className="col-lg-4 col-form-label">Typ ćwiczenia</label>
                           <div className="col-lg-6">
                           <Select
                              onChange={(e) => setNewExercise(
                                 (prevState) => ({
                                    ...prevState,
                                    exerciseType: e.value
                              }))}
                              options={exerciseTypes}
                              getOptionLabel={(option) => option.label}
                                    style={{
                                    lineHeight: "40px",
                                    color: "#7e7e7e",
                                    paddingLeft: " 15px",
                            }} />
                           </div>
                           </div>

                        <div className="form-group row">
                        <label className="col-lg-4 col-form-label">Główny zaangażowany mięsień</label>
                           <div className="col-lg-6">
                           <Select
                              defaultValue={muscles[getSelectedMuscle()]}
                              onChange={(e) => setNewExercise(
                                 (prevState) => ({
                                    ...prevState,
                                    targetMuscle: e.value
                              }))}
                              options={muscles}
                              getOptionLabel={(option) => option.label}
                                    style={{
                                    lineHeight: "40px",
                                    color: "#7e7e7e",
                                    paddingLeft: " 15px",
                            }} />
                           </div>
                           </div>

                           <div className="form-group row">
                        <label className="col-lg-4 col-form-label">Dodatkowo zaangażowane mięśnie</label>
                           <div className="col-lg-6">
                           <Select
           closeMenuOnSelect={false}
           defaultValue={newExerciseAdditionalMuscles}
           isMulti
           options={setAvailableMuscles()}   
           getOptionLabel={(option) => option.label}
           onChange={(e) => handleChangeNewExerciseAdditionalMuscles(e)}    
        />
                           </div>
                           </div>

                           
                     </div>

                  </div>
                  <center>
                     <button  className="btn btn-primary" onClick={() => handleAddNewExercise()}>
                        Dodaj ćwiczenie do bazy ćwiczeń
                         </button>
                  </center>   
      </Tab>
    </Tabs>
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
