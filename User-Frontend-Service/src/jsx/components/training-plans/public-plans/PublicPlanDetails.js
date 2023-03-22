import React, {useState, useEffect} from 'react'
import { useSelector } from 'react-redux';
import { Link , useHistory, useParams} from "react-router-dom";
import { getTrainingPlan, copyTrainingPlan } from '../../../../services/TrainingPlanService';
import swal from "sweetalert";
import Select from "react-select";
import DayTable from '../DayTable';
import { Button } from "react-bootstrap";
import { store } from '../../../../store/store';
import TrainingPlanComments from '../TrainingPlanComments';

export default function PublicPlanDetails() {
   const [refresh, setRefresh] = useState(1)
   const state = store.getState();
   let userId = state.auth.auth.userId;

   const [isUserPlan, setIsUserPlan] = useState(false)

   const history = useHistory()
    const [isEditable, setIsEditable] = useState(false)
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

    const [isLoading, setIsLoading] = useState(true);

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
                planRates: plan.planRates.map(x => ({id: x.id, description: x.description, rate: x.rate, rateDate: new Date(x.creationDate).toLocaleDateString()})),
                trainingUnits: plan.trainingUnits.map(unit => ({id: unit.id, trainingDay: unit.trainingDay, trainingUnitParts:
                    unit.trainingUnitParts.map(part => ({id: part.id, exerciseId: part.exerciseId, exerciseName: part.exerciseName, targetMuscle: part.targetMuscle,
                     additionalMuscles: part.additionalMuscles, //.map(m => mapMuscleGroup(m)), 
                seriesRepetitionsDetails: part.seriesRepetitionsDetails.map(s => ({seriesNumber: s.seriesNumber, repetitionsNumber: s.repetitionsNumber}))}))}))
            }

            if(tmp.planCreatorUserId === userId){
               setIsUserPlan(true)
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



    function assignDefaultTrainingDays(trainingDays) {
      let tmp = []
       trainingDays.forEach(selected => {
         tmp.push( trainingDaysOptions.find(day => day.value == selected));
      })
      return tmp;
    }

    const addToMyPlans = () => {

        swal({
            title: "Kopiowanie planu treningowego",
            text: "Czy napewno chcesz dodać plan treningowy do swoich planów ?",
            icon: "warning",
            buttons: [
              'Nie, anuluj',
              'Tak, dodaj plan'
            ],
            dangerMode: false,
          }).then(function(isConfirm) {
            if (isConfirm) {
                copyTrainingPlan(trainingPlan.id, userId)
               .then(response => {
                  if(response.status == 201){
                     swal('Sukces', 'Plan został dodany do twoich planów treningowych !', "success");
                     setTimeout(history.push(`/training-plans`), 1500);
                  } else {
                     swal('Błąd', 'Wystąpił błąd w trakcie kopiowania planu treningowego.', "error");
                  }
                  
               })
               .catch(error =>{
                  swal('Błąd', 'Wystąpił błąd w trakcie kopiowania planu treningowego !', "error");
                  console.log(error)})
   
            } 
          });
    }


    return !trainingPlan ? null : (
        <div>
       
            <div className="card">
            
                     {!isUserPlan && <div className="card-header">
                  
                        <h4 className="card-title">Szczegóły planu treningowego</h4>
                        
                      <Button className="m-sm-right mt-2 d-flex align-items-center justify-content-center btn" variant="outline-primary" onClick = {() => {addToMyPlans()}}>
                               Dodaj do moich planów treningowych
                        </Button>
                        
                     </div> }

                     {isUserPlan && <div className="card-header">
                  
                  <h4 className="card-title">Szczegóły planu treningowego</h4>
                
                <Button className="m-sm-right mt-2 d-flex align-items-center justify-content-center btn" variant="outline-primary" onClick = {() => {history.replace(`training-plan/${trainingPlan.id}`)}}>
                         Przejdz do szczegółów swojego planu
                  </Button>
                  
               </div> }
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
                                 />
                                </div>
                             </div>
       <div className="col-lg-12 mb-2">
          <div className="form-group">
             <label className="text-label">Dni treningowe</label>
             <Select
             isDisabled = {true}
           closeMenuOnSelect={false}
           defaultValue={trainingPlan.trainingDays}
           value = {trainingPlan.trainingDays}
           isMulti
           options={trainingDaysOptions}   
        />
        
          </div>
       </div>
       <div className="col-lg-12 mb-2">
          <div className="form-group">
             <label className="text-label">Priorytet planu treningowego</label>
             <Select      
             isDisabled = {true}
                        defaultValue={planMusclePriorirtyOptions.find(x => x.value === trainingPlan.planPriority)}               
                        value={planMusclePriorirtyOptions.find(x => x.value === trainingPlan.planPriority)}
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
           isDisabled = {true}
                        defaultValue={planStateOptions.find(x => x.value === trainingPlan.isPublic)}
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
             editable = {false}
                defaultValue={trainingPlan.description}
                value = {trainingPlan.description}
                type="text"
                name="place"
                className="form-control"
             />
          </div>
       </div>
    </div>
    <div className="mt-4">
                        {!isUserPlan && <center>
                     <Button className="btn btn-primary mb-1 mr-1" onClick = {() => {addToMyPlans()}}>
                               Dodaj do moich planów treningowych
                        </Button> 
                        </center> }

                        {isUserPlan && <center>
                     <Button className="btn btn-primary mb-1 mr-1" onClick = {() => {history.replace(`/training-plan/${trainingPlan.id}`)}}>
                               Przejdz do szczegółów swojego planu
                        </Button> 
                        </center> }
                        </div>
 </div>
 
</div>


           <div className="row">



           <div className="col-lg-12">
                 <div className="card">
                    <div className="card-header">
                       <h4 className="card-title">Szkielet planu treningowe</h4>
              
                    </div>
            <div className="row">
              </div>                    

                   {!isLoading && <div className="card-body">
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "MONDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "MONDAY")} trainingDay = {"MONDAY"}  />} 
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "TUESDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "TUESDAY")} trainingDay = {"TUESDAY"} />} 
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "WEDNESDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "WEDNESDAY")} trainingDay = {"WEDNESDAY"} />} 
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "THURSDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "THURSDAY")} trainingDay = {"THURSDAY"}  />} 
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "FRIDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "FRIDAY")} trainingDay = {"FRIDAY"}  />} 
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "SATURDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "SATURDAY")} trainingDay = {"SATURDAY"} />} 
                      { (trainingPlan.trainingUnits.filter(i => i.trainingDay === "SUNDAY").length > 0) && <DayTable trainingUnits = {trainingPlan.trainingUnits.filter(unit => unit.trainingDay === "SUNDAY")} trainingDay = {"SUNDAY"} />} 

                    </div>  }
                 </div>
              </div> 
              <div className = "col-lg-12">
                <div classname = "card">
         
                   {!isLoading && <TrainingPlanComments trainingPlanId={trainingPlan.id} comments={trainingPlan.planRates}  refresh={() => setRefresh((prevState) => prevState+1)} />}
                    </div>
                </div>
           </div> 
                    
           </div>
          );
}