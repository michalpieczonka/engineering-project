import React, { useState, useEffect } from "react";
import { Link, useHistory } from "react-router-dom";
import { Dropdown, Modal, Button } from "react-bootstrap";

import loadable from "@loadable/component";
import pMinDelay from "p-min-delay";
import {getWorkoutVolumeOneRepStatistics, getWorkoutTotalStatistics} from "../../../services/WorkoutService"
import {getCurrentTrainingPlan} from "../../../services/TrainingPlanService";
import { store } from "../../../store/store";
import { mapTrainingDay } from "../workout-create/CommonMappers";
import Select from "react-select";
import { mapPlanPriority } from "../workout-create/CommonMappers";

import dumbells_icon_s from "../../../images/plans/dumbells_icon_s.png";
import PageTitle from "../../layouts/PageTitle";


const BasicStatsChart = loadable(() => pMinDelay(import("../workout-statistics/BasicBar"), 50));

const VolumeOneRepChart = loadable(() =>
   pMinDelay(import("../workout-statistics/VolumeRepLine"), 50)
);



const Statistic = () => {
   const [activeModal, setActiveModal] = useState(false);
   const history = useHistory()
   const state = store.getState();
   let userId = state.auth.auth.userId;
   
   const [basicStats, setBasicStats] = useState([]);
   const [currentTrainingPlan, setCurrentTrainingPlan] = useState({});
   const [hasCurrent, setHasCurrent] = useState(false);
   const [planLoaded, setPlanLoaded] = useState(false);
   const [refresh, setRefresh] = useState(1)

   const [selectedTrainingUnitId, setSelectedTrianingUnitId] = useState('invalid')
   const [selectedExerciseId, setSelectedExerciseId] = useState('invalid')

   useEffect(() => { 

      if(planLoaded === true){

         getWorkoutVolumeOneRepStatistics(userId, currentTrainingPlan.id).then((response) => {
            let tmp = []
            const base = response.data.map(x => tmp.push(
               {
                  workoutId: x.workoutId, 
                  trainingUnitId: x.trainingUnitId,
                  trainingDay: mapTrainingDay(x.trainingDay), 
                  sessionDate: new Date(x.sessionFinishTime).toLocaleDateString(),
                  details: x.volumeExerciseDetails.map(z => ({
                     exerciseId: z.exerciseId,
                     exerciseName: z.exerciseName,
                     totalVolume: z.totalVolume,
                     oneRepMax: z.oneRepMax
                  }))
               }
                  ))
                  setBasicStats(tmp)
            
         }).catch(error => swal("Błąd", "Nie udało się załadować podstawowych statystyk planu treningowego", "error"))
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
               planPriority: tmp.planPriority,
               planType: tmp.planType,
               trainingUnits: tmp.trainingUnits.map(u => ({value: u.id,  label: mapTrainingDay(u.trainingDay)}))
            }
            setCurrentTrainingPlan(plan)
            setPlanLoaded(true)
            setRefresh((prev) => prev+1)

         }
      }).catch(error => swal("Błąd", "Nie udało się załadować szczegółów twojego planu treningowego", "error"))
     }, []); 



const handleChangeVolumeChart = (trainingUnitId) => {
   setSelectedTrianingUnitId(trainingUnitId)
   if (selectedExerciseId !== 'invalid' && selectedTrainingUnitId !== 'invalid'){
      handleVolumeChartData()
   }
   
}

const handleChangeExerciseVolumeChart = (exerciseId) => {
   setSelectedExerciseId(exerciseId)
   if (selectedExerciseId !== 'invalid' && selectedTrainingUnitId !== 'invalid'){
      handleVolumeChartData()
   }
}

const [volumeRepXData, setVolumeRepXData] = useState('empty')
const [volumeRepYData, setVolumeRepYData] = useState('empty')
const [oneRepMaxXdata, setOneRepMaxXdata] = useState('empty')
const [oneRepMaxYdata, setOneRepMaxYdata] = useState('empty')
const [oneVolume, setOneVolume] = useState('volume')
const [renderVolume, setRenderVolume] = useState(1)

const handleVolumeChartData = () => {
   if(oneVolume === 'volume'){
      let initialData = basicStats.filter(x => x.trainingUnitId === selectedTrainingUnitId)
      let data = initialData.map(x => x.details.map(y => ({xData: x.sessionDate, yData: y.totalVolume}))).flat().sort((a,b) => a.xData > b.xData ? 1 : -1)
      let xData = data.map(x => x.xData)
      setVolumeRepXData(xData)
      let yData = data.map(x => x.yData)
      setVolumeRepYData(yData)
      setRenderVolume((prev) => prev+1)
   }

   if(oneVolume === '1RM'){
      let initialData = basicStats.filter(x => x.trainingUnitId === selectedTrainingUnitId)
      let data = initialData.map(x => x.details.map(y => ({xData: x.sessionDate, yData: y.oneRepMax}))).flat().sort((a,b) => a.xData > b.xData ? 1 : -1)
      let xData = data.map(x => x.xData)
      setOneRepMaxXdata(xData)
      let yData = data.map(x => x.yData)
      setOneRepMaxYdata(yData)
      setRenderVolume((prev) => prev+1)
   }

}

const [seriesData, setSeriesData] = useState('empty')
const [repsData, setRepsData] = useState('empty')
useEffect(() => { 

   if(planLoaded === true){

      getWorkoutTotalStatistics(userId, currentTrainingPlan.id).then((response) => {
         let tmp = []
         const base = response.data.map(x => tmp.push(
            {
               exerciseId: x.exerciseId,
               exerciseName: x.exerciseName,
               totalSeries: x.totalSeries,
               totalReps: x.totalReps
            }))             
      let Seriesdata = tmp.map(x => ({label: x.exerciseName, value: x.totalSeries}))
      setSeriesData(Seriesdata)
      let Repsdata = tmp.map(x => ({label: x.exerciseName, value: x.totalReps}))
      setRepsData(Repsdata)

         
      }).catch(error => swal("Błąd", "Nie udało się załadować podstawowych statystyk planu treningowego", "error"))


   }
  }, [refresh]);

   return (
      <>
        <PageTitle activeMenu="Statystyki treningu" motherMenu="Twoj trening" />
         <div className="row">
            <div className="col-xl-9 col-xxl-8">
               <div className="row">
                  <div className="col-xl-12">
                     <div className="card">
                        {/* <div className="card-header d-sm-flex d-block pb-0 border-0"> */}
                        <div className="card-header flex-wrap pb-0 border-0">
                           <div className="d-flex align-items-center">
                              <span className="p-3 mr-3 rounded bg-warning">
                              </span>
                              <div className="mr-auto pr-3">
                                 <h4 className="text-black fs-20">Podstawowe statystyki planu treningowego</h4>
                                 <p className="fs-13 mb-0 text-black">
                                    Wykres progresu objętości i siły maksymalnej (1RM) w ciągu ostatnich 20 treningów
                                    <br />
                                    {(selectedTrainingUnitId !== 'invalid' && selectedExerciseId !== 'invalid') &&  <p className="fs-13 mb-0 text-black">Dane wyświetlane dla dnia treningowego:  
                                     {basicStats.filter(x =>x.trainingUnitId === selectedTrainingUnitId)[0].trainingDay} oraz ćwiczenia {basicStats.filter(x =>x.trainingUnitId === selectedTrainingUnitId).map(z => z.details.exerciseId === selectedExerciseId)[0].exerciseName}</p>}
                                 </p>
                              </div>
                              
                           </div>
                           {planLoaded && <Dropdown className="dropdown mt-sm-0 mt-3">
                              <Dropdown.Toggle
                                 as="button"
                                 variant=""
                                 className="btn rounded border border-light dropdown-toggle"
                              >
                                 Parametry wykresu
                              </Dropdown.Toggle>
                              <Dropdown.Menu className="dropdown-menu dropdown-menu-right">
                                 <Dropdown.Item  onClick={() => setOneVolume('1RM')}>Powtórzenie maksymalne (1RM) </Dropdown.Item>
                                 <Dropdown.Item  onClick={() => setOneVolume('volume')}>Objętość całkowita (tonaż)</Dropdown.Item>
                              </Dropdown.Menu>
                           </Dropdown> }
         <div>

         {planLoaded &&  <span> Jednostka treningowa:  <Select                 
                        onChange={e => handleChangeVolumeChart(e.value)}
                        value = {basicStats.filter(x => x.trainingUnitId === selectedTrainingUnitId).map(x => ({label: x.trainingDay, value: x.trainingUnitId}))[0]}
                        options={basicStats.map(x => ({label: x.trainingDay, value: x.trainingUnitId}) ).filter((item, index, self) => 
                        self.findIndex(t => (t.value === item.value)) === index)}
                        styles={{
                           control: (base, state) => ({
                             ...base,
                             height: '50px',
                             width: '180px'
                           })
                         }}
                        style={{                         
                           lineHeight: "40px",
                           color: "#7e7e7e",
                           paddingLeft: " 0px",
                        }}  /> </span>}



                                        {selectedTrainingUnitId !== 'invalid' && <span> Ćwiczenie:  <Select                 
                        onChange={e => handleChangeExerciseVolumeChart(e.value)}
                        value = {basicStats.filter(x => x.trainingUnitId === selectedTrainingUnitId).map(x => x.details).flat().filter(x => x.exerciseId === selectedExerciseId).map(x => ({label: x.exerciseName, value: x.exerciseId}))[0]}
                       // value = {basicStats.filter(x => x.exerciseId === selectedExerciseId).map(x => x.details).flat().map(x => ({label: x.exerciseName, value: x.exerciseId}))}
                        options={basicStats.filter(x => x.trainingUnitId === selectedTrainingUnitId).map(x => x.details).flat().map(x => ({label: x.exerciseName, value: x.exerciseId})).filter((item, index, self) => 
                        self.findIndex(t => (t.value === item.value)) === index)}
                        styles={{
                           control: (base, state) => ({
                             ...base,
                             height: '50px',
                             width: '180px'
                           })
                         }}
                        style={{
                           lineHeight: "40px",
                           color: "#7e7e7e",
                           paddingLeft: " 15px",
                        }}  /> </span>}

         </div>

</div>

                          
                        
                        <div className="card-body pb-0">
                          {(volumeRepXData !== 'empty' && volumeRepYData !== 'empty' && oneVolume === 'volume') && <VolumeOneRepChart values={volumeRepXData} dates={volumeRepYData} text={'Całkowity tonaż'}/>}
                          {(oneRepMaxXdata !== 'empty' && oneRepMaxYdata !== 'empty' && oneVolume === '1RM') && <VolumeOneRepChart values={oneRepMaxXdata} dates={oneRepMaxYdata} text={'Obliczony wynik maksymalny w 1 powtórzeniu (1RM)'}/>}
                        </div>
                     </div>
                  </div>
                  <div className="col-xl-12">
                     <div className="card">
                        <div className="card-header d-sm-flex d-block pb-0 border-0">
                           <div className="d-flex align-items-center">
                              <span className="p-3 mr-3 rounded bg-info">
                              
                              </span>
                              <div className="mr-auto pr-3">
                                 <h4 className="text-black fs-20">Statystyki ćwiczeń - powtórzenia/ćwiczenie</h4>
                                 <p className="fs-13 mb-0 text-black">
                                    Wykres przedstawiający statystyki ilości powtórzen z wykonywanych sesji treningowych <br /> dla każdego z ćwiczeń zdefiniowanych w planie treningowych.
                                 </p>
                              </div>
                           </div>
                        </div>
                        <div className="card-body pb-0">
                        {repsData !== 'empty' &&  <BasicStatsChart xdata={repsData.map(x => x.value)} ydata={repsData.map(x  =>  x.label)} text={'Łączna liczba powtórzeń'} />}
                        </div>
                     </div>
                  </div>
                  <div className="col-xl-12">
                     <div className="card">
                        <div className="card-header d-sm-flex d-block pb-0 border-0">
                           <div className="d-flex align-items-center">
                              <span className="p-3 mr-3 rounded bg-secondary">
                               
                              </span>
                              <div className="mr-auto pr-3">
                                 <h4 className="text-black fs-20">Statystyki ćwiczeń - serie/ćwiczenie</h4>
                                 <p className="fs-13 mb-0 text-black">
                                 Wykres przedstawiający statystyki liczby serii z wykonywanych sesji treningowych <br /> dla każdego z ćwiczeń zdefiniowanych w planie treningowych. <br />
                                 W celu zachowania balansu staraj się, by wykresy były podobnej wysokości !
                                 </p>
                              </div>
                           </div>
                        </div>
                        <div className="card-body pb-0">
                           {seriesData !== 'empty' &&  <BasicStatsChart xdata={seriesData.map(x => x.value)} ydata={seriesData.map(x  =>  x.label)} text={'Łączna liczba serii'} />}
                       
                        </div>
                     </div>
                  </div>
               </div>
            </div>
            <div className="col-xl-3 col-xxl-4">
               <div className="row">

               <div className="col-xl-12 col-md-6">
                     <div className="card">
                        <div className="card-header flex-wrap border-0 pb-0">
                           <h4 className="text-black fs-20 mb-3">Aktualnie wykonywany plan treningowy</h4>
                        </div>
                        <div className="card-body">
                           <div className="d-flex mb-sm-5 mb-3">
                              <div className="d-inline-block relative donut-chart-sale mr-3">
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
                  
                           <Button className="m-sm-auto mt-2 d-flex align-items-center justify-content-center btn" variant="outline-primary"
                            onClick={() => setShowChangePlanModal(true)}>
                            Ustaw plan treningowy
                        </Button>
                        </div>}
                            
                              </div>
            
                           </div>
                 
     
                        </div>
                     </div>
                  </div>

              
       
               </div>
            </div>
         </div>
      </>
   );
};

export default Statistic;
