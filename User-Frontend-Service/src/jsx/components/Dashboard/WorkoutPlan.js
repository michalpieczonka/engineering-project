import React, {useEffect, useState, Fragment} from 'react'
import { Link, useHistory } from "react-router-dom";
import "react-calendar/dist/Calendar.css";
import { Button, Modal, Dropdown } from "react-bootstrap";
import WorkoutUnitSelectorModal from "../workout/WorkoutUnitSelectorModal";
import { getCurrentTrainingPlan, getAllUserTrainingPlans,  setCurrentUserTrianingPlan } from "../../../services/TrainingPlanService";
import Select from "react-select";
import { store } from "../../../store/store";
import { mapTrainingDay } from '../workout-create/CommonMappers';
import {getAllUserWorkouts} from '../../../services/WorkoutService';
import { mapPlanPriority } from '../workout-create/CommonMappers';
import WorkoutsPagTable from './WorkoutsPagTable';
import Paginations from './Paginations';
import PageTitle from '../../layouts/PageTitle';


import dumbells_icon_s from "../../../images/plans/dumbells_icon_s.png";

const WorkoutPlan = () => {
   const history = useHistory()
   const state = store.getState();
   let userId = state.auth.auth.userId;
   const [value, onChange] = useState(new Date());
   const [addPlan, setAddPlan] = useState(false);
   const [activeModal, setActiveModal] = useState(false);
   const [activePickerModal, setActivePickerModal] = useState(false);

   const[currentTrainingPlan, setCurrentTrainingPlan] = useState({});
   const[selectedTrainingUnit, setSelectedTrianingUnit] = useState('');
   const[trainingUnits, setTrainingUnits] = useState([]);

   const[currentPage, setCurrentPage] = useState(1);
   const[workoutsPerPage, setWorkoutsPerPage] = useState(8);

   const [workouts, setWorkouts] = useState([])
   const indexOfLastWorkout = currentPage * workoutsPerPage;
   const indexOfFirstWorkout = indexOfLastWorkout - workoutsPerPage;
   const currentWorkouts = workouts.slice(indexOfFirstWorkout, indexOfLastWorkout);


   const [refresh, setRefresh] = useState('loading')
   const [workoutsLoaded, setWorkoutsLoaded] = useState(false)

   const handleSelectTrainingUnit = (e)  =>  {
      let tmp = {value: e.value, label: e.label}
      setSelectedTrianingUnit(tmp)
   }


   const [hasCurrent, setHasCurrent] = useState(false)
   const [currentNotDefinedMessage, setCurrentNotDefinedMessage] = useState('Brak aktualnego planu treningowego')
   const [currentUpdatedPlan, setCurrentUpdatedPlan] = useState('')
   const [trainingPlans, setTrainingPlans] = useState([])
   const [showChangePlanModal, setShowChangePlanModal] = useState(false);

   const handleChangeCurrentTrainingPlan = e => {
      let tmp = {value: e.value, label: e.label}
      setCurrentUpdatedPlan(tmp)
      console.log(currentUpdatedPlan)
   }

   const updateCurrentTrainingPlan = () => {
      setShowChangePlanModal(false)
      setCurrentUserTrianingPlan(userId, currentUpdatedPlan.value)
      .then(response => {
         if(response.status === 200){
            swal('Sukces', 'Aktualny plan treningowy został zmieniony !', "success");
            setRefresh((prevState) => prevState+1)
         } else {
            swal('Błąd', 'Nie udało się zmienić aktualnie wykonywanego planu treningowego, wystąpił błąd.', "error");
         }
         
      })
      .catch(error =>{
         swal('Błąd', 'Wystąpił błąd w trakcie zmiany planu treningowego.', "error");
         console.log(error)})
   }



   const startNewWorkout = () => {
      let trainingUnitId = trainingUnits.find(u => u.id == selectedTrainingUnit.value).id
      let trainingPlanId = currentTrainingPlan.id
      setActivePickerModal(false)
      history.push(`training-plan/${trainingPlanId}/perform/${trainingUnitId}`);
   }

   useEffect(() => { 
      getAllUserTrainingPlans(userId).then((response) => {
         let tmp = []
         const allTrainingPlans = response.data.map(x => tmp.push({id: x.id, title: x.title, description: x.description, numberOfTrainingDays: x.numberOfTrainingDays, planPriority: x.planPriority, isPublic: x.isPublic, planType: x.planType}))
         setTrainingPlans(tmp)
         setRefresh('loaded')
      }).catch(error => swal("Błąd", "Nie udało się załadować Twoich planów treningowych", "error"))
     }, []);

     useEffect(() => {
      if(refresh == 'loaded') {
         getAllUserWorkouts(userId).then((response) => {
            let tmp = []
            const allWorkoutss = response.data.map(x => tmp.push({id: x.workoutId, trainingPlanId: x.trainingPlanId, 
               title: trainingPlans.find(plan => plan.id === x.trainingPlanId).title , trainingDay: mapTrainingDay(x.trainingDay), startedAt: x.startedAt, finishedAt: x.finishedAt, rate: x.workoutAssessment.personalRate}))
            setWorkouts(tmp)
            setWorkoutsLoaded(true)
         }).catch(error => swal("Błąd", "Nie udało się załadować Twoich planów treningowych", "error"))
      } 
     }, [refresh]);


   useEffect(() => { 
      getCurrentTrainingPlan(userId).then((response) => {
         let tmp = response.data
         if(tmp.id != 0){
            setHasCurrent(true)
            let plan = {
               id: tmp.id,
               title: tmp.title,
               description: tmp.planDescription,
               numberOfTrainingDays: tmp.numberOfTrainingDays,
               planPriority: mapPlanPriority(tmp.planPriority),
               planType: tmp.planType,
               trainingUnits: tmp.trainingUnits.map(u => ({value: u.id,  label: mapTrainingDay(u.trainingDay)}))
            }

            let tmpTrainingUnits = tmp.trainingUnits.map(unit => ({id: unit.id, trainingDay: unit.trainingDay, trainingUnitParts:
               unit.trainingUnitParts.map(part => ({id: part.id, exerciseId: part.exerciseId, exerciseName: part.exerciseName, targetMuscle: part.targetMuscle,
                additionalMuscles: part.additionalMuscles, //.map(m => mapMuscleGroup(m)), 
           seriesRepetitionsDetails: part.seriesRepetitionsDetails.map(s => ({seriesNumber: s.seriesNumber, repetitionsNumber: s.repetitionsNumber}))}))}))
            setTrainingUnits(tmpTrainingUnits)
            setCurrentTrainingPlan(plan)
         }
      }).catch(error => swal("Błąd", "Nie udało się załadować szczegółów twojego planu treningowego", "error"))
     }, [refresh]); 

     const paginate = (pageNumber) => setCurrentPage(pageNumber)

   return (
      <React.Fragment>
          <PageTitle activeMenu="Historia twoich sesji treningowych" motherMenu="Twoj trening" />
         <div className="row">
            <div className="col-xl-3 col-xxl-4">
               <div className="row">
                  <div className="col-xl-12">
                     <div className="card flex-xl-column flex-sm-row flex-column">
                     <div className="card-body">

                         <Button
                         as ="a"
                           variant="primary"
                           className="bg-primary text-white text-center p-4 fs-20 d-block rounded"
                           onClick={() => setActivePickerModal(true)}
                        >
                           Rozpocznij nowy trening
                        </Button>
                           </div>
                           

                           
                        <div className="card-header pb-0 border-0">
                           <div className="mr-auto pr-3">
                              <h4 className="text-black fs-20">
                                 Aktualnie wykonywany plan treningowy
                              </h4>
                           </div>

                        </div>
                        <Modal className="fade" show={showChangePlanModal}>
                           <Modal.Header>
                              <Modal.Title>Zmiana aktualnego planu treningowego</Modal.Title>
                              <Button
                                 variant=""
                                 className="close"
                                 onClick={() => setShowChangePlanModal(false)}
                              >
                                 <span>&times;</span>
                              </Button>
                           </Modal.Header>

                           <Modal.Body> 

                           <div className="col-lg-12 mb-2">
          <div className="form-group">
          <h3> Wybierz nowy plan treningowy </h3>
          <label className="text-label">Aktualnie wykonywany plan treningowy: <br /> <b>Id: {currentTrainingPlan.id}</b>, Tytuł: <b>{currentTrainingPlan.title}</b> </label>
          
          <Select                 
                        onChange={e => handleChangeCurrentTrainingPlan(e)}
                        value = {currentUpdatedPlan}
                        options={trainingPlans.map(plan => ({value: plan.id, label: plan.title}))}
                        style={{
                           lineHeight: "40px",
                           color: "#7e7e7e",
                           paddingLeft: " 15px",
                        }}  />
          </div>
       </div>
                           </Modal.Body>

                           <Modal.Footer>
                              <Button
                                 onClick={() => setShowChangePlanModal(false)}
                                 variant="danger light"
                              >
                                 Zamknij
                              </Button>
                              <Button  onClick={() => updateCurrentTrainingPlan()} variant="primary">Zapisz zmiany</Button>
                           </Modal.Footer>
                        </Modal>

                       {hasCurrent && <div className="card-body">
                           <div className="media mb-3">
                              <Link to={"/training-plan/"+currentTrainingPlan.id}>
                                 <img
                                    src={dumbells_icon_s}
                                    alt=""
                                    className="rounded mr-3"
                                    width={86}
                                 />
                              </Link>
                              <div className="media-body">
                                 <h6 className="fs-16 font-w500">
                                    <Link
                                       to={"/training-plan/"+currentTrainingPlan.id}
                                       className="text-black"
                                    >
                                       {currentTrainingPlan.title}
                                    </Link>
                                 </h6>
                                 <span className="fs-14">{currentTrainingPlan.description}</span>
                              </div>
                           </div>
                           <ul className="m-md-auto mt-2 pr-4">
                              <li className="mb-2 text-nowrap">
                              <i class="fa fa-list-ol" aria-hidden="true"></i>
                                 <span className="fs-14 text-black text-nowrap font-w500">
                                   Dni treningowych {currentTrainingPlan.numberOfTrainingDays}
                                 </span>
                              </li>
                              <li className="mb-2 text-nowrap">
                              <i class="fa fa-hand-o-right" aria-hidden="true"></i>
                                 <span className="fs-14 text-black text-nowrap font-w500">
                                   Typ planu {currentTrainingPlan.planType}
                                 </span>
                              </li>
                                              <li className="text-nowrap">
                                       <i
                                          className="fa fa-star-o mr-3 scale5 text-warning"
                                          aria-hidden="true"
                                       />
                                       <span className="text-nowrap fs-14 text-black font-w500">
                                          Priorytet: {mapPlanPriority(currentTrainingPlan.planPriority)} 
                                       </span>
                                    </li>

                           </ul>
                           <br />

                           <Button className="m-sm-auto mt-2 d-flex align-items-center justify-content-center btn" variant="primary">
                           <Link to={"/training-plan/"+currentTrainingPlan.id}> Wyświetl szczegóły</Link>
                            </Button>
                        < br />
                             <Button
                           variant="primary"
                           className="m-sm-auto mt-2 d-flex align-items-center justify-content-center btn"
                           onClick={() => setShowChangePlanModal(true)}
                        >
                           Zmień plan
                        </Button>
                        </div>}

                        {!hasCurrent && <div className="card-body">
                           <div className="media mb-3">

                              <div className="media-body">
                                 <h6 className="fs-16 font-w500">
         
                                       Nie posiadasz aktualnie zdefiniowanego planu treiningowego
                             
                                 </h6>
                                 <span className="fs-14">{currentTrainingPlan.description}</span>
                              </div>
                           </div>
                  
                           <Button className="m-sm-auto mt-2 d-flex align-items-center justify-content-center btn" variant="outline-primary">
                           <Link to={"/profile"}> Ustaw plan treningowy</Link>
                        </Button>
                        </div>}


                        {/* <div className="card-body border-bottom pb-4 p-2 event-calender col-md-6 col-lg-6 col-lg-12">
                           <Calendar onChange={onChange} value={value} />
                        </div> */}

                        <Modal className="fade" show={activePickerModal}>
                           <Modal.Header>
                              <Modal.Title>Rozpoczynanie treningu</Modal.Title>
                              <Button
                                 variant=""
                                 className="close"
                                 onClick={() => setActivePickerModal(false)}
                              >
                                 <span>&times;</span>
                              </Button>
                           </Modal.Header>

                           <Modal.Body> 

                           <div className="col-lg-12 mb-2">
          <div className="form-group">
          <h3> Wybierz jednostkę treningową</h3>
          <label className="text-label">Aktualnie wykonywany plan treningowy: <br /> <b>Id: {currentTrainingPlan.id}</b>, Tytuł: <b>{currentTrainingPlan.title}</b> </label>
          <label className="text-label">Wybierz dzień treningowy, z którego trening chcesz wykonać: </label>
          <Select                 
                        onChange={e => handleSelectTrainingUnit(e)}
                        value = {selectedTrainingUnit}
                        options={currentTrainingPlan.trainingUnits}
                        style={{
                           lineHeight: "40px",
                           color: "#7e7e7e",
                           paddingLeft: " 15px",
                        }}  />
          </div>
       </div>
                           </Modal.Body>

                           <Modal.Footer>
                              <Button
                                 onClick={() => setActivePickerModal(false)}
                                 variant="danger light"
                              >
                                 Zamknij
                              </Button>
                              <Button  onClick={() => startNewWorkout()} variant="primary">Rozpocznij trening</Button>
                           </Modal.Footer>
                        </Modal>

                     </div>
                  </div>
               </div>
            </div>
            <div className="col-xl-9 col-xxl-8">
               <div className="row">
                  <div className="col-xl-12">
                     <div className="card plan-list">
                        <div className="card-header d-sm-middle d-block pb-0 border-0">
                           <div className="mr-auto pr-3">
                              <center>
                              <h4 className="text-black fs-25">Ostatnie sesje treningowe</h4>
                              <p className="fs-20 mb-0 text-black">
                                 Lista ostatnio wykonywanych treningów
                              </p></center>
                           </div>
                           {/* <Dropdown className="mt-sm-0 mt-3">
                              <Dropdown.Toggle
                                 variant=""
                                 as="button"
                                 className="btn rounded border text-black border-light dropdown-toggle"
                              >
                                 Unfinished
                              </Dropdown.Toggle>
                              <Dropdown.Menu className="dropdown-menu-right">
                                 <Dropdown.Item>Link 1</Dropdown.Item>
                                 <Dropdown.Item>Link 2</Dropdown.Item>
                                 <Dropdown.Item>Link 3</Dropdown.Item>
                              </Dropdown.Menu>
                           </Dropdown> */}
                        </div>
                        <div className="card-body">
                           <WorkoutsPagTable workouts={currentWorkouts} workoutsLoaded={workoutsLoaded}/>
                           <Paginations workoutsPerPage = {workoutsPerPage} totalWorkouts={workouts.length} paginate={paginate} />
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </React.Fragment>
   );
};

export default WorkoutPlan;
