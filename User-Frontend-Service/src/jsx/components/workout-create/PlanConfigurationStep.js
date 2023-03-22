import React, { useState } from 'react'
 import Select from "react-select";
 import { bindActionCreators } from 'redux';
 import { useDispatch, useSelector } from 'react-redux';
import {  assignName, addTrainingDay, specifyPlanPublicity, assignPlanPriority, specifyAdditionalInformations, specifyPlanType, specifyPlanFinishDate } from '../../../store/actions/WorkoutWizardActions'
import {DatePicker } from "@y0c/react-datepicker";

export default function PlanConfigurationStep() {

   const planState = useSelector((state) => state.workoutwizard);
   

   const dispatch = useDispatch();
   

   const AC = bindActionCreators(    { assignName, addTrainingDay, specifyPlanPublicity, assignPlanPriority, specifyAdditionalInformations, specifyPlanType, specifyPlanFinishDate  },    dispatch)
 
    const trainingDaysOptions = [
        {value: "MONDAY", label: "Poniedziałek"}, 
        {value: "TUESDAY", label: "Wtorek"},
        {value: "WEDNESDAY", label: "Środa"},
        {value: "THRUSTDAY", label: "Czwartek"},
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

    const planTypeOptions = [
         {value: "HYBRID", label: "Hybrydowy"},
         {value: "PUSH_PULL", label: "Siłowy - push/pull"},
         {value: "UPPER_LOWER", label: "Siłowy - góra/dół"},
         {value: "FULL_BODY_WORKOUT", label: "Siłowy - Full body workout"}
   ];

    const [trainingDays, setTrainingDays] = useState(assignDefaultTrainingDays())
   

    const handleChangeTrainingDays = e => {
            dispatch(addTrainingDay(e))
    }

    const handleChangePlanName = e => {
      dispatch(assignName(e.target.value))
    }

    const handleChangePlanPublicity = e => {
      dispatch(specifyPlanPublicity(e.value))
    }

    const handleChangePlanPriority = e => {
      dispatch(assignPlanPriority(e.value))
    }

    const handleChangePlanFinishDate = e => {
      let tmp  =e.$d.toISOString().slice(0, 10)
      setStartDate(tmp)
      console.log('Data:'+tmp)
      dispatch(specifyPlanFinishDate(tmp))
    }

    const handleChangeAdditionalInformations = e => {
      dispatch(specifyAdditionalInformations(e.target.value))
    }

    const handleChangePlanType = e => {
      dispatch(specifyPlanType(e.value))
    }

    function assignDefaultTrainingDays() {
      const tmp = [];
      planState.trainingDays.forEach(selected => {
         tmp.push( trainingDaysOptions.find(day => day.value == selected));
      })
      return tmp;
    }

    const [startDate, setStartDate] = useState(new Date());
    

  return (
         <div className="card">
                     <div className="card-header">
                        <h4 className="card-title">Parametry podstawowe planu  treningowego </h4>
                     </div>
                     <div className="card-body">
                        <div className="row">
                            <div className="col-lg-12 mb-2">
                                  <div className="form-group">
                                    <label className="text-label">Nazwa planu treningowego</label>
                                    <input
                                    value = {planState.planName}
                                     type="text"
                                     name="planName"
                                    className="form-control"
                                    placeholder="..."
                                    required
                                    onChange={handleChangePlanName}
                                 />
                                </div>
                             </div>
       <div className="col-lg-12 mb-2">
          <div className="form-group">
             <label className="text-label">Dni treningowe</label>
             <Select
           closeMenuOnSelect={false}
           defaultValue={trainingDays}
           isMulti
           options={trainingDaysOptions}   
           onChange={(e) => handleChangeTrainingDays(e)}    
        />
        
          </div>
       </div>
       <div className="col-lg-13 mb-2">
          <div className="form-group">
             <label className="text-label">Docelowa data zakończenia</label>
             <DatePicker selected={startDate} onChange={date => handleChangePlanFinishDate(date)} />
          </div>
       </div>
       <div className="col-lg-12 mb-2">
          <div className="form-group">
             <label className="text-label">Priorytet planu treningowego</label>
             <Select                     
                        defaultValue={planMusclePriorirtyOptions.find(x => x.value === planState.planPriorirty)}
                        onChange={handleChangePlanPriority}
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
             <label className="text-label">Typ planu treningowego</label>
             <Select                     
                        onChange={handleChangePlanType}
                        options={planTypeOptions}
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
                        defaultValue={planStateOptions.find(x => x.value === planState.isPublic)}
                        onChange={handleChangePlanPublicity}
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
                value = {planState.additionalInformations}
                type="text"
                name="place"
                className="form-control"
                onChange={handleChangeAdditionalInformations}
             />
          </div>
       </div>
    </div>
 </div>
</div>
  )

  
}


