import React, { useState} from 'react'
import { Button, Modal, Tab, Tabs } from "react-bootstrap";
import swal from "sweetalert";



export default function ExerciesEditModal({setTrainingUnitPartDetails, currentUnitPartDetails, disableModal, handleSaveUnitPartDetails}) {

   const [largeModal, setLargeModal] = useState(true);
  const [numberOfSeries, setNumberOfSeries] = useState('')
  const [trainingUnitDetails, setTrainingUnitDetails] = useState(currentUnitPartDetails.seriesRepetitionsDetails)

  const onlyNumbers = /^\d+$/;

   const handleNumberOfSeries = (numberOfSeries, index, event) => {
      console.log("Serie: "+numberOfSeries)
      console.log("index: "+index)
      console.log(trainingUnitDetails)
      if (numberOfSeries > 10){
         swal('Oops', 'Liczba serii wieksza od 10 jest niedozwolona.', "error");
      } else {
         if(numberOfSeries !== 0 && index === -1 && event === -1){       
            setNumberOfSeries(numberOfSeries);
            var tmpUnitDetails = [];
            for(let i=0; i<numberOfSeries; i++){
               tmpUnitDetails.push( {seriesNumber: i+1, repetitionsNumber: '' })
            }
            setTrainingUnitDetails(tmpUnitDetails)
   
         } else if(numberOfSeries === -1){
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


   const confirmChangesAndSave = () => {
      swal({
         title: "Modyfikacja szczegółów ćwiczenia",
         text: "Czy napewno chcesz zmienić zakres serii i powtórzeń ?",
         icon: "warning",
         buttons: [
           'Nie, anuluj',
           'Tak, zmień'
         ],
         dangerMode: true,
       }).then(function(isConfirm) {
         if (isConfirm) {
           handleSaveUnitPartDetails(trainingUnitDetails)
           disableModal()
         } 
       });
   }
  
  return (
    <Modal
    className="fade bd-example-modal-lg"
    show={largeModal}
    size="lg"
 >
   
    <Modal.Header>
       <Modal.Title>Edycja ćwiczenia</Modal.Title>
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
      <Tab eventKey="addToPlan" title="Edytuj szczegóły ćwiczenia">
      <div className="card-body">
         <div class="col">
         <h4 class="mm-b-0">Edytuj szczegóły ćwiczenia</h4>
         <span>{currentUnitPartDetails.exerciseName}</span>
         <br />
         <br />
         </div>
     
                     <div className="basic-form">
                        <div className="form-group row">
                                    <label
                                       className="col-lg-4 col-form-label"
                                       htmlFor="val-digits"
                                    >
                                       Liczba serii{" "}
                                    </label>
                                    <div className="col-lg-6">
                                       <input
                                          defaultValue={trainingUnitDetails.length}
                                          onChange={(e) => handleNumberOfSeries(e.target.value, -1, -1)}
                                          type="text"
                                          className="form-control"
                                          id="val-digits"
                                          name="val-digits"
                                       />
                                    </div>
                                 </div>

                                    {trainingUnitDetails.length > 0 && trainingUnitDetails.sort((a, b) => a.seriesNumber - b.seriesNumber).map((input, index) => {
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
                                             //defaultValue={ input.repetitionsNumber} 
                                             value = {input.repetitionsNumber}
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
                     <button  className="btn btn-primary" onClick={() => confirmChangesAndSave()}>
                        Zapisz zmiany
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
          Anuluj i zamknij
       </Button>
    </Modal.Footer>
 </Modal>
  )
}
