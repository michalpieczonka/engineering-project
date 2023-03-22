import React, {useState, useEffect} from 'react'
import { useSelector } from 'react-redux';
import { Link , useHistory, useParams} from "react-router-dom";
import { getTrainingPlan, updateTrainingPlanDetails, deleteTrainingUnitPart, addTrainingUnitPart, updateTrainingUnitPart } from '../../../services/TrainingPlanService';
import swal from "sweetalert";
import Select from "react-select";
import DayTable from './DayTable'
import { Button } from "react-bootstrap";
import EditBodyyHighlighter from './EditBodyyHighlighter';

export default function TrainingPlanDetails() {
   const [refresh, setRefresh] = useState(1)

   const history = useHistory()
    const [isEditable, setIsEditable] = useState(false)
    const [detailsEditable, setDetailsEditable] = useState(false)
    const [trainingPlan, setTrainingPlan] = useState({
        id: 0,
        title:  '',
        description: '',
        numberOfTrainingDays: '',
        trainingDays: [],
        planPriority: '',
        planType:'',
        preferredTrainingIntership: '',
        planCreatorUserId: '',
        planUsersIds: [],
        isPublic: '',
        planRates: [],
        trainingUnits: [{
            trainingUnitParts: []
        }]
    });
    const params = useParams();
    let trainingPlanId = params.id

    const [showEditModal, setShowEditModal] = useState(false)
    const [isLoading, setIsLoading] = useState(true);

    const handleUpdateExercise = (exercise) => {
         console.log(exercise)
    }

    const handleAddNewExexrcise = (exercise, trainingDay) => {
         console.log("przyszlo w details: "+exercise+ " "+trainingDay)
         let trainingUnitId = trainingPlan.trainingUnits.find(unit => unit.trainingDay === trainingDay).id
         let request = {
            exerciseId: exercise.exerciseId,
            seriesRepetitionsDetails: exercise.seriesRepetitionsDetails
         }
         addTrainingUnitPart(trainingPlan.id, trainingUnitId, request)
         .then(response => {
            if(response.status == 201){
               swal('Sukces', 'Plan treningowy został zaktualizowany !', "success");
            } else {
               swal('Błąd', 'Nie udało się zaktualizować planu treningowego, wystąpił błąd.', "error");
            }
            setRefresh((prevState) => prevState+1)
            
         })
         .catch(error =>{
            swal('Błąd', 'Wystąpił błąd w trakcie dodawania nowego ćwiczenia do planu treningowego.', "error");
            console.log(error)})
    }

    useEffect(() => { 
        setIsLoading(true)
        getTrainingPlan(trainingPlanId).then((response) => {       
            let plan = response.data
            let tmp = {
                id: plan.id,
                title:  plan.title,
                description: plan.planDescription,
                numberOfTrainingDays: plan.numberOfTrainingDays,
                trainingDays: assignDefaultTrainingDays(plan.trainingDays),
                planPriority: plan.planPriority,
                planType: 'HYBRID', //todo: fix it
                preferredTrainingIntership: plan.preferredTrainingIntership,
                planCreatorUserId: plan.planCreatorUserId,
                planUsersIds: plan.planUsersIds,
                isPublic: plan.public,
                planRates: plan.planRates.map(x => ({id: x.id, description: x.description, rate: x.rate})),
                trainingUnits: plan.trainingUnits.map(unit => ({id: unit.id, trainingDay: unit.trainingDay, trainingUnitParts:
                    unit.trainingUnitParts.map(part => ({id: part.id, exerciseId: part.exerciseId, exerciseName: part.exerciseName, targetMuscle: part.targetMuscle,
                     additionalMuscles: part.additionalMuscles, //.map(m => mapMuscleGroup(m)), 
                seriesRepetitionsDetails: part.seriesRepetitionsDetails.map(s => ({seriesNumber: s.seriesNumber, repetitionsNumber: s.repetitionsNumber}))}))}))
            }

            setTrainingPlan(tmp)
            setIsLoading(false)
        }).catch(error => swal("Błąd", "Nie udało się załadować szczegółów planu treningowego", "error").then(() => history.push('/training-plans')))
       }, [refresh]);

    const trainingDaysOptions = [
        {value: "MONDAY", label: "Poniedziałek"}, 
        {value: "TUESDAY", label: "Wtorek"},
        {value: "WEDNESDAY", label: "Środa"},
        {value: "THURSDAY", label: "Czwartek"},
        {value: "FRIDAY", label: "Piątek"},
        {value: "SATURDAY", label: "Sobota"},
        {value: "SUNDAY", label: "Niedziela"},
    ];

    const planMusclePriorirtyOptions = [
        {value: "LOWER", label: "Nogi"},
        {value: "CHEST", label: "Klatka piersiowa"},
        {value: "ARMS", label: "Ramiona"},
        {value: "BACK", label: "Plecy/Grzbiet"},
        {value: "SHOULDERS", label: "Barki"},
        {value: "UNSPECIFIED", label: "Brak priorytetu"},
    ];

    const planStateOptions = [
        {value: true, label: "Publiczny"},
        {value: false, label: "Prywatny"}
    ];


    const handleUpdateTrainingPlan = () => {
      setIsEditable(false)
      let updateTrainingPlanRequest = {
         title: trainingPlan.title,
         planDescription: trainingPlan.description,
         trainingDays: trainingPlan.trainingDays.map(day => day.value),
         planPriority: trainingPlan.planPriority,
         planType: trainingPlan.planType,
         isPublic: trainingPlan.isPublic
      }
      updateTrainingPlanDetails(trainingPlan.id, updateTrainingPlanRequest)
      .then(response => {
         if(response.status == 204){
            swal('Sukces', 'Plan treningowy został zaktualizowany !', "success");
         } else {
            swal('Błąd', 'Nie udało się zaktualizować planu treningowego, wystąpił błąd.', "error");
         }
         setRefresh((prevState) => prevState+1)
         
      })
      .catch(error =>{
         swal('Błąd', 'Wystąpił błąd w trakcie aktualizacji planu treningowego.', "error");
         console.log(error)})
    }

    const handleChangePlanName = e => {
      if (isEditable) {
         setTrainingPlan( {
            ... trainingPlan,
            title: e.target.value
         })
      }

    }

    const handleChangePlanPublicity = e => {
     if(isEditable){
      setTrainingPlan(
         {
            ... trainingPlan,
            isPublic: e.value
         }
      )
     }
    }

    const handleChangePlanPriority = e => {
      if(isEditable){
         setTrainingPlan(
            {
               ... trainingPlan,
               planPriority: e.value
            }
         )
        }
    }

    const handleChangeTrainingDays = (e) => {
      if(isEditable){
         setTrainingPlan(
            {
               ... trainingPlan,
               trainingDays: Array.isArray(e) ? e.map(x => trainingDaysOptions.find(day => day.value === x.value) ) : []
            }
         )
      }
   }


   const handleChangeTrainingUnitPart = (seriesRepetitionsDetails, trainingUnitPartId, trainingUnitId) => {
      // const updatedArray = trainingPlan.trainingUnits.map((trainingUnit) => {
      //    if(trainingUnitId === trainingUnit.id){
      //       trainingUnit.trainingUnitParts.forEach((unitPart) => {
      //             if(unitPart.id === trainingUnitPartId){
      //                unitPart.seriesRepetitionsDetails = seriesRepetitionsDetails
      //             }
      //       });           
      //    }
      //    return trainingUnit;
      // });
      let updateTrainingUnitPartRequest = {
         seriesRepetitionsDetails: seriesRepetitionsDetails
      }

      updateTrainingUnitPart(trainingPlan.id, trainingUnitId, trainingUnitPartId, updateTrainingUnitPartRequest)
      .then(response => {
         if(response.status == 204){
            swal('Sukces', 'Plan treningowy został zaktualizowany !', "success");
            setRefresh((prevState) => prevState+1)
         } else {
            swal('Błąd', 'Nie udało się zaktualizować planu treningowego, wystąpił błąd.', "error");
         }
         
      })
      .catch(error =>{
         swal('Błąd', 'Wystąpił błąd w trakcie aktualizacji planu treningowego.', "error");
         console.log(error)})
      
      // setTrainingPlan(
      //    {
      //       ... trainingPlan,
      //       trainingUnits: updatedArray
      //    }
      // )

   }



   const handleRemoveExercise = (trainingUnitId, trainingUnitPartId) => {
      swal({
         title: "Usuwanie ćwiczenia z planu treningowego",
         text: "Czy napewno chcesz usunąć ćwiczenie z planu ?",
         icon: "warning",
         buttons: [
           'Nie, anuluj',
           'Tak, usuń'
         ],
         dangerMode: true,
       }).then(function(isConfirm) {
         if (isConfirm) {
            deleteTrainingUnitPart(trainingPlan.id, trainingUnitId, trainingUnitPartId)
            .then(response => {
               if(response.status == 204){
                  swal('Sukces', 'Plan treningowy został zaktualizowany !', "success");
                  setRefresh((prevState) => prevState+1)
               } else {
                  swal('Błąd', 'Nie udało się zaktualizować planu treningowego, wystąpił błąd.', "error");
               }
               
            })
            .catch(error =>{
               swal('Błąd', 'Wystąpił błąd w trakcie usuwania jednostki treningowej.', "error");
               console.log(error)})
         } 
       });
   
   }

    const handleChangeAdditionalInformations = e => {
      if (isEditable) {
         setTrainingPlan( {
            ... trainingPlan,
            description: e.target.value
         })
      }
    }

    function assignDefaultTrainingDays(trainingDays) {
      let tmp = []
       trainingDays.forEach(selected => {
         tmp.push( trainingDaysOptions.find(day => day.value == selected));
      })
      return tmp;
    }


    return !trainingPlan ? null : (
        <div>
       
            <div className="card">
            
                     <div className="card-header">
                  
                        <h4 className="card-title">Szczegóły planu treningowego</h4>
                       {!isEditable && <Button className="m-sm-right mt-2 d-flex align-items-center justify-content-center btn" variant="outline-primary" onClick = {() => {setIsEditable(true)}}>
                               Edytuj szczegóły
                        </Button> }
                      
                           
                        {isEditable &&<center>
                          <div className="mt-4"> <Button className="btn btn-primary mb-1 mr-1" variant="outline-primary" onClick = {() => {handleUpdateTrainingPlan()}}>
                               Zapisz zmiany
                        </Button> 
                        <Button className="btn btn-primary mb-1 mr-1" onClick = {() => { setIsEditable(false)}}>
                               Anuluj
                        </Button> </div> </center>} 
                        
                     </div>
                     <div className="card-header">
                  
                  <h4 className="card-title">Parametry podstawowe planu  treningowego </h4>
               </div>
                     <div className="card-body">
                        <div className="row">
                            <div className="col-lg-12 mb-2">
                                  <div className="form-group">
                                    <label className="text-label">Nazwa planu treningowego</label>
                                    <input
                                    editable = {isEditable}
                                    defaultValue={trainingPlan.title}
                                    value = {trainingPlan.title}
                                     type="text"
                                     name="planName"
                                    className="form-control"
                                    placeholder="..."
                                    required
                                    onChange={(e) => handleChangePlanName(e)}
                                 />
                                </div>
                             </div>
       <div className="col-lg-12 mb-2">
          <div className="form-group">
             <label className="text-label">Dni treningowe</label>
             <Select
             isDisabled = {!isEditable}
           closeMenuOnSelect={false}
           defaultValue={trainingPlan.trainingDays}
           value = {trainingPlan.trainingDays}
           isMulti
           options={trainingDaysOptions}   
           onChange={(e) => handleChangeTrainingDays(e)}    
        />
        
          </div>
       </div>
       <div className="col-lg-12 mb-2">
          <div className="form-group">
             <label className="text-label">Priorytet planu treningowego</label>
             <Select      
             isDisabled = {!isEditable}
                        defaultValue={planMusclePriorirtyOptions.find(x => x.value === trainingPlan.planPriority)}               
                        value={planMusclePriorirtyOptions.find(x => x.value === trainingPlan.planPriority)}
                        onChange={e => handleChangePlanPriority(e)}
                        options={planMusclePriorirtyOptions}
                        style={{
                           lineHeight: "40px",
                           color: "#7e7e7e",
                           paddingLeft: " 15px",
                        }}  />
          </div>
       </div>
       <div className="col-lg-12 mb-2">
          <div className="form-group">
          <label className="text-label">Widoczność planu (publiczny/tylko dla mnie) </label>
          <Select
          isDisabled = {!isEditable}
                        defaultValue={planStateOptions.find(x => x.value === trainingPlan.isPublic)}
                        onChange={e => handleChangePlanPublicity(e)}
                        value = {planStateOptions.find(x => x.value === trainingPlan.isPublic)}
                        options={planStateOptions}
                        style={{
                           lineHeight: "40px",
                           color: "#7e7e7e",
                           paddingLeft: " 15px",
                        }}  />
          </div>
       </div>
       <div className="col-lg-12 mb-2">
          <div className="form-group">
             <label className="text-label">
                Dodatkowe informacje
             </label>
             <input
                defaultValue={trainingPlan.description}
                value = {trainingPlan.description}
                type="text"
                name="place"
                className="form-control"
                onChange={(e) => handleChangeAdditionalInformations(e)}
             />
          </div>
       </div>
    </div>
    <div className="mt-4">
                        <center>
                      {isEditable && <Button className="btn btn-primary mb-1 mr-1" onClick = {() => {handleUpdateTrainingPlan()}}>
                               Zapisz zmiany
                        </Button> }

                        {isEditable && <Button className="btn btn-primary mb-1 mr-1" onClick = {() => { setIsEditable(false)}}>
                               Anuluj
                        </Button> }
                        </center>
                        </div>
 </div>
 
</div>


           <div className="row">



           <div className="col-lg-12">
                 <div className="card">
                    <div className="card-header">
                       <h4 className="card-title">Szkielet planu treningowe</h4>
                       {!detailsEditable && <Button className="m-sm-right mt-2 d-flex align-items-center justify-content-center btn" variant="outline-primary" onClick = {() => {setDetailsEditable(true)}}>
                               Edytuj szczegóły jednostek treningowych
                        </Button> }
                        {detailsEditable && <Button className="m-sm-right mt-2 d-flex align-items-center justify-content-center btn" variant="outline-primary" onClick = {() => {setDetailsEditable(false)}}>
                               Zapisz zmiany
                        </Button> }
              
                    </div>
            <div className="row">
                    { detailsEditable &&<div className="col-lg-12">
                 <div className="card">
                    <div className="card-header">
                       <h4 className="card-title">Wybierz ćwiczenia na każdą grupę mięsniową</h4>
                    </div>
                    <div className="card-body">
                           <EditBodyyHighlighter trainingPlan = {trainingPlan} addExerciseHandler = {handleAddNewExexrcise} />
                    </div>
                 </div>
              </div> }
              </div>                    

                   {!isLoading && <div className="card-body">
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "MONDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "MONDAY")} trainingDay = {"MONDAY"} editable = {detailsEditable} exRemoveHandler = {handleRemoveExercise}
                      exUpdateHandler = {handleChangeTrainingUnitPart} />} 
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "TUESDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "TUESDAY")} trainingDay = {"TUESDAY"} editable = {detailsEditable} 
                      exRemoveHandler = {handleRemoveExercise}  exUpdateHandler = {handleChangeTrainingUnitPart}/>} 
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "WEDNESDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "WEDNESDAY")} trainingDay = {"WEDNESDAY"} editable = {detailsEditable} 
                      exRemoveHandler = {handleRemoveExercise}  exUpdateHandler = {handleChangeTrainingUnitPart}/>} 
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "THURSDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "THURSDAY")} trainingDay = {"THURSDAY"} editable = {detailsEditable} 
                      exRemoveHandler = {handleRemoveExercise}  exUpdateHandler = {handleChangeTrainingUnitPart} />} 
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "FRIDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "FRIDAY")} trainingDay = {"FRIDAY"} editable = {detailsEditable}
                       exRemoveHandler = {handleRemoveExercise}  exUpdateHandler = {handleChangeTrainingUnitPart} />} 
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "SATURDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "SATURDAY")} trainingDay = {"SATURDAY"} editable = {detailsEditable} 
                      exRemoveHandler = {handleRemoveExercise}  exUpdateHandler = {handleChangeTrainingUnitPart} />} 
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "SUNDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "SUNDAY")} trainingDay = {"SUNDAY"} editable = {detailsEditable} 
                      exRemoveHandler = {handleRemoveExercise}  exUpdateHandler = {handleChangeTrainingUnitPart} />} 

                    </div>  }
                 </div>
              </div> 
           </div> 
                    
           </div>
          );
}