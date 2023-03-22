import React, {useState, useEffect} from 'react'
import Nouislider from "nouislider-react";
import { Link , useHistory, useParams} from "react-router-dom";
import { getTrainingPlan} from '../../../services/TrainingPlanService';
import swal from "sweetalert";
import { Button, Modal, Col, Row, Accordion, Card, Table,ProgressBar,Badge, Tab, Nav } from "react-bootstrap";
import WorkoutUnitTemplate from './WorkoutUnitTemplate';
import { store } from "../../../store/store";
import {getLatestUserWorkoutForTrainingUnit, getWorkoutTemplate, saveWorkout} from '../../../services/WorkoutService';
import {mapTrainingDay} from '../../components/workout-create/CommonMappers'
import moment from 'moment';
import BarChartt from './BarChartt';
import "nouislider/distribute/nouislider.css";


export default function WorkoutPerformTemplate() {
   const params = useParams();
   const state = store.getState();

   let userId = state.auth.auth.userId;
   let trainingPlanId = params.trainingPlanId
   let trainingUnitId = params.trainingUnitId

   const[isLoading, setIsLoading] = useState(true)

   const [refresh, setRefresh] = useState(1)

   const [template, setTemplate] = useState({})

   const [currentSession, setCurrentSession] = useState({})
   const [activeRateModal, setActiveRateModal]  = useState(false)
   const [comment, setComment] = useState("")
   const [rate, setRate] = useState(5)
   const date = moment();

   const handleUpdateCurrentSession = (trainingUnitPartId, repetitions) => {
      console.log('zmiana dla' + trainingUnitPartId + ' na ' + repetitions)
      const updatedWorkoutParts = currentSession.workoutParts.map(workoutPart => {
         if(workoutPart.trainingUnitPartId === trainingUnitPartId){
            return {...workoutPart, seriesRepetitionsDetails: repetitions}
         }
         return workoutPart;
      });

      setCurrentSession({...currentSession, workoutParts: updatedWorkoutParts})
   } 


   const [latestWorkout, setLatestWorkout] = useState({})
   const [latestWorkoutLoaded, setLatestWorkoutLoaded] = useState(false)
   const [trainingPlan, setTrainingPlan] = useState(null)
   const [trainingMinutes, setTrainingMinutes] = useState(0)
   

   useEffect(() => { 
      getLatestUserWorkoutForTrainingUnit(userId, trainingUnitId).then((response) => {
         let tmp = response.data
         if(response.data.workoutId != "-1"){
            let workoutPlan = {
               workoutId: tmp.workoutId,
               trainingPlanId: tmp.trainingPlanId,
               trainingUnitId: tmp.trainingUnitId,
               startedAt: tmp.startedAt,
               finishedAt: tmp.finishedAt,
               workoutAssessment: {rate: tmp.workoutAssessment.personalRate, comment: tmp.workoutAssessment.additionalComment},
               trainingDay: mapTrainingDay(tmp.trainingDay),
               workoutParts: tmp.workoutParts.map(part => (
                  {unitPartId: part.trainingUnitPartId, exerciseId: part.exerciseId, exerciseName: part.exerciseName,
                  targetSeriesRepetitionsDetails: part.seriesRepetitionsDetails.map(s => (
                     {seriesNumber: s.seriesNumber, targetRepetitionsNumber: s.targetSeriesRepetitionsNumber, performedRepetitionsNumber: s.performedRepetitionsNumber, pastWorkoutUsedWeight: s.usedWeight})
                     ).sort((a,b) => { return a.seriesNumber - b.seriesNumber})
                  }))
   
            }
           setLatestWorkoutLoaded(true)
           setLatestWorkout(workoutPlan)
         }

         })
      .catch(error => swal("Błąd", "Nie udało się załadować szczegółów ostatniej sesji treningowej z tożsamej jednostki treningowej", "error"))
     }, []); 


     useEffect(() => { 
      if(!isLoading){
         getTrainingPlan(currentSession.trainingPlanId).then((response) => {
            let plan = response.data
            let tmp = {
                id: plan.id,
                title:  plan.title,
                description: plan.planDescription
            }
            setTrainingPlan(tmp)
            })
         .catch(error => swal("Błąd", "Nie udało się załadować szczegółów planu treningowego", "error"))

      }
     }, [isLoading]); 

     const prepareRepetitionsChartData = () => {
      let sums = []
      latestWorkout?.workoutParts?.forEach((item) => {
        let sum = 0
        item.targetSeriesRepetitionsDetails.forEach((item) => {
            sum += item.performedRepetitionsNumber
        })
        sums.push(sum)
    })
    return sums;
     }
  
     const prepareSeriesChartData = () => {
      let sums = []
      latestWorkout?.workoutParts?.forEach((item) => {
        let sum = 0
        item.targetSeriesRepetitionsDetails.forEach((item) => {
            sum += item.seriesNumber
        })
        sums.push(sum)
    })
    return sums;
     }
  
     const prepareVolumeChartData = () => {
      let sums = []
      latestWorkout?.workoutParts?.forEach((item) => {
        let sum = 0
        item.targetSeriesRepetitionsDetails.forEach((item) => {
            sum += item.pastWorkoutUsedWeight
        })
        sums.push(sum)
    })
    return sums;
     }

     
   const defaultAccordion = [
      {
         title: "Zalecenia dotyczące realizacji treningu siłowego w ramach treningu zdrowotnego",
         text:
            "Aby uzyskać korzystne efekty zdrowotne, należy wykonywać ćwiczenia siłowe co najmniej 2 razy w tygodniu, w nienastępujących po sobie dniach. Powinny one angażować wszystkie większe grupy mięśniowe, poprawiając ich silę i wytrzymałość. Czas trwania treningu to około 20–60 minut.",
         bg: "primary",
      },
      {
         title: "Prawidłowa rozgrzewka - pamiętaj o niej",
         text:
            "Prawidłowa rozgrzewka w treningu siłowym powinna trwać około 10–12 minut i składać się z dwóch części. I część – rozgrzewka ogólna (5–6 minut). Polega na wykonywaniu wysiłku ciągłego o charakterze tlenowym z małą intensywnością (np. bieg w miejscu, na bieżni, jazda na rowerze stacjonarnym itp.). Jak nazwa wskazuje, ma ona na celu ogólne przygotowanie organizmu do wysiłku poprzez zwiększenie przepływu krwi przez mięśnie i zwiększenie ogólnej ciepłoty ciała.II część – rozgrzewka specyficzna. Ma na celu „dogrzanie” poszczególnych partii ciała, które poddawane będą obciążeniu. W jej trakcie wykonuje się ćwiczenia kształtujące (takie jak krążenia ramion w staniu, w opadzie, skłony tułowia, skrętoskłony, przysiady itp.) oraz niektóre ćwiczenia z założonego planu treningowego, ale wykonywane tylko po jednej serii, z niewielkim obciążeniem zewnętrznym (np. z samym gryfem od sztangi). ",

         bg: "info",
      },
      {
         title: "Trening to nie wszystko",
         text:
            "Trening siłowy, choć niezbędny, stanowi zaledwie 1/3 drogi do sukcesu w postaci osiągnięcia zakładanych celów treningowych. Pozostała część przypada na regenerację, która obejmuje właściwą dietę i odpoczynek (czynny lub bierny). Trening jest „tylko” bodźcem wytrącającym organizm z równowagi czynnościowej (homeostazy). Właściwe zmiany adaptacyjne umożliwiające poprawę sprawności fizycznej i szeroko pojmowanego zdrowia zachodzą dopiero w okresie potreningowym. Jeżeli jednak proces regeneracyjny zostanie zaburzony przez nieodpowiedni odpoczynek i/lub niewłaściwą dietę, to zamiast postępów treningowych dojdzie do stagnacji lub przeciążenia i przetrenowania organizmu. Wobec powyższego, aby wysiłek nie poszedł na marne, warto zwrócić uwagę na styl życia poza treningami. Prawidłowy sposób żywienia, dostarczający odpowiednią ilość płynów, energii i składników odżywczych, oraz właściwy odpoczynek (przynajmniej 1–2 dni przerwy pomiędzy treningami, zachowanie higieny pracy i odpoczynku, 7–8 godzin snu) dopełnią proces treningowy i pozwolą cieszyć się zdrowiem oraz sprawnością fizyczną.",

         bg: "success",
      },
   ];
   const [activeWithoutSpace, setActiveWithoutSpace] = useState(0);



   useEffect(() => { 
      getWorkoutTemplate(userId, trainingPlanId, trainingUnitId).then((response) => {
         let tmp = response.data
         let templ = {
            trainingPlanId: tmp.trainingPlanId,
            trainingUnitId: tmp.trainingUnitId,
            trainingDay: mapTrainingDay(tmp.trainingDay),
            trainingUnitParts: tmp.trainingUnitParts.map(part => (
               {unitPartId: part.unitPartId, exerciseId: part.exerciseId, exerciseName: part.exerciseName,
               targetSeriesRepetitionsDetails: part.targetSeriesRepetitionsDetails.map(s => (
                  {seriesNumber: s.seriesNumber, repetitionsNumber: s.targetRepetitionsNumber, pastWorkoutRepetitionsNumber: s.pastWorkoutRepetitionsNumber, pastWorkoutUsedWeight: s.pastWorkoutUsedWeight})
                  ).sort((a,b) => { return a.seriesNumber - b.seriesNumber})
               }))

         }
        console.log(templ)
        setTemplate(templ)
        setCurrentSession({
         trainingPlanId: trainingPlanId,
         trainingUnitId: trainingUnitId,
         startedAt:  moment(date.toDate()).toISOString(),
         finishedAt: moment(date.toDate()).toISOString(),
         trainingDay: tmp.trainingDay,
         workoutAssessment: {additionalComment: comment, personalRate: rate},
         workoutParts: tmp.trainingUnitParts.map(part => ({
            trainingUnitPartId: part.unitPartId,
            exerciseId: part.exerciseId,
            exerciseName: part.exerciseName,
            seriesRepetitionsDetails: []
         }))         
        })
        setIsLoading(false)
         })
      .catch(error => swal("Błąd", "Nie udało się załadować szablonu planu treningowego", "error").then(() => history.push('/workout-center')))
     }, []); 

   const history = useHistory()

   const validateFullPerformedSeession = () => {
      let isFull = true;
      currentSession.workoutParts.forEach(workoutPart => {
         if(workoutPart.seriesRepetitionsDetails.length != template.trainingUnitParts.find(x => x.unitPartId === workoutPart.trainingUnitPartId).targetSeriesRepetitionsDetails.length){
            isFull = false;
         }
      })
      return isFull;
   }


   const cancelWorkoutSession = () => {
      swal({
         title: "Anulowanie sesji treningowej",
         text: "Czy napewno chcesz zakończyć trening i wyjść ?\nZmiany zostaną utracone !",
         icon: "warning",
         buttons: [
           'Nie, anuluj',
           'Tak, wyjdź'
         ],
         dangerMode: false,
       }).then(function(isConfirm) {
         if (isConfirm) {
            history.push('/workout-center')
         } 
       });
   }


   const saveWorkoutSession = () => {
      if (!validateFullPerformedSeession()){
         swal('Błąd', 'Nie udało się zakończyć planu treningowego. Posiadasz nieuzupełnione ćwiczenia.\nProszę dokończ trening !', "error");
      } else {
         swal({
            title: "Zapisywanie sesji treningowej",
            text: "Czy napewno chcesz zakończyć trening i zapisać sesję?",
            icon: "warning",
            buttons: [
              'Nie, anuluj',
              'Tak, zapisz'
            ],
            dangerMode: false,
          }).then(function(isConfirm) {
            if (isConfirm) {
               setActiveRateModal(true)

            } 
          });

      }
   }

   const confirmSaveSession = () => {
      // setCurrentSession({...currentSession, finishedAt: moment().toISOString().slice(0, -1)})
      // setCurrentSession({...currentSession, workoutAssessment: {additionalComment: comment, personalRate: rate}})
      console.log('przed zapisem' + comment)
      console.log('przed zapisem' + rate)
      let temp = {additionalComment: comment, personalRate: rate}
      setCurrentSession(prevState => ({
         ...prevState,
         finishedAt: moment().toISOString().slice(0, -1),
         workoutAssessment: temp,
       }));
       console.log(currentSession)
       saveSession()
   }

   const saveSession = () => {
      saveWorkout(userId, currentSession)
      .then(response => {
         if(response.status == 201){
            swal('Sukces', 'Sesja treningowa została zapisana !', "success");
            setTimeout(history.push(`/workout-center`), 1500);
         } else {
            swal('Błąd', 'Nie udało się zapisać sesji treningowej, wystąpił błąd.', "error");
         }
         
      })
      .catch(error =>{
         swal('Błąd', 'Wystąpił błąd w trakcie zapisu sesji treningowej.', "error");
         console.log(error)})
   }


    return !template ? null : (
        <div>
                            <Modal className="fade" show={activeRateModal}>
                           <Modal.Header>
                              <Modal.Title>Zapisywanie treningu - ocena trudności</Modal.Title>
                              <Button
                                 variant=""
                                 className="close"
                                 onClick={() => setActiveRateModal(false)}
                              >
                                 <span>&times;</span>
                              </Button>
                           </Modal.Header>

                           <Modal.Body> 

                           <div className="col-lg-12 mb-2">
          <div className="form-group">
            <center><h3> Oceń swój trening </h3>
          <label className="text-label">Jak trudny był trening ?<br /> Zapisz swoją subiektywną ocenę wykorzystując poniższy suwak: </label>
            <div id = "basic-slider">
            <Nouislider
                              start={0}
                              pips={{ mode: "count", values: 5 }}
                              clickablePips
                              range={{
                                 min: 1,
                                 max: 10,
                              }}
                              onSlide={(e) => setRate(Math.round(e))}
                           /></div>
          <br />
          <br />
          <label className="text-label">Masz swoje notatki ?<br />Dodaj dodatkowe uwagi i miej do nich dostęp przy następnym treningu ! </label>
          <input
                                    value = {comment}
                                     type="text"
                                     name="numberOfCurrentRepetitions"
                                    className="form-control"
                                    placeholder="Dodaj komentarz do treningu"
                                    required
                                    onChange={(e) => setComment(e.target.value)}
                                 /> </center>
          </div>
       </div>
                           </Modal.Body>

                           <Modal.Footer>
                              <Button
                                 onClick={() => setActiveRateModal(false)}
                                 variant="danger light"
                              >
                                 Zamknij
                              </Button>
                              <Button  onClick={() => confirmSaveSession()} variant="primary">Zapisz trening</Button>
                           </Modal.Footer>
                        </Modal>

            <div className="card">
                     <div className="card-header">
                        <h4 className="card-title">Nowa sesja treningowa</h4>
                     </div>
                     <div className="card-body">

                        <div className="row">


      {latestWorkoutLoaded &&   <div className='col-xl-4 col-lg-12 col-sm-12'>
          <div className='card'>
            <div className='card-header border-0 pb-0'>
              <h2 className='card-title'>Szczegóły ostatniej sesji treningowej</h2>
            </div>
            <div className='card-body pb-0'>
              <ul className='list-group list-group-flush'>
                <li className='list-group-item d-flex px-0 justify-content-between'>
                  <strong>Plan treningowy:</strong>
                  {trainingPlan && <span className='mb-0'>{trainingPlan.title}</span>}
                  {!trainingPlan && <span className='mb-0'>Trwa ładowanie</span>}
                </li>
                <li className='list-group-item d-flex px-0 justify-content-between'>
                  <strong>Dzień treningowy:</strong>
                  <span className='mb-0'>{latestWorkout.trainingDay}</span>
                </li>
                <li className='list-group-item d-flex px-0 justify-content-between'>
                  <strong>Data rozpoczęcia:</strong>
                  <span className='mb-0'>{moment(latestWorkout.startedAt).format("DD.MM.YYYY HH:mm")}</span>
                </li>
                <li className='list-group-item d-flex px-0 justify-content-between'>
                  <strong>Data zakończenia:</strong>
                  <span className='mb-0'>{moment(latestWorkout.finishedAt).format("DD.MM.YYYY HH:mm")}</span>
                </li>
                <li className='list-group-item d-flex px-0 justify-content-between'>
                  <strong>Subiektywna ocena trudności:</strong>
                  <span className='mb-0'>{latestWorkout?.workoutAssessment?.rate}</span>
                </li>
                <li className='list-group-item d-flex px-0 justify-content-between'>
                  <strong>Dodatkowy komentarz:</strong>
                  <span className='mb-0'>{latestWorkout?.workoutAssessment?.comment}</span>
                </li>
              </ul>
            </div>
            <div className='card-footer pt-0 pb-0 text-center'>
              <div className='row'>
                <div className='col-4 pt-3 pb-3 border-right'>
                  <h3 className='mb-1 text-primary'>{latestWorkout?.workoutParts?.reduce((acc, workoutPart) => acc + workoutPart.targetSeriesRepetitionsDetails.reduce((acc, series) => acc + series.performedRepetitionsNumber, 0), 0)}</h3>
                  <span>Powtórzeń łącznie</span>
                </div>
                <div className='col-4 pt-3 pb-3 border-right'>
                  <h3 className='mb-1 text-primary'>{latestWorkout?.workoutParts?.length}</h3>
                  <span>Ćwiczeń</span>
                </div>
                <div className='col-4 pt-3 pb-3'>
                  <h3 className='mb-1 text-primary'>{trainingMinutes}</h3>
                  <span>Minut wysiłku</span>
                </div>
              </div>
            </div>
          </div>
        </div> }

        {latestWorkoutLoaded &&<div className='col-xl-8 col-xxl-8 col-lg-12 col-sm-12'>
          <div id='user-activity' className='card'>
            <Tab.Container defaultActiveKey='reps'>
              <div className='card-header border-0 pb-0 d-sm-flex d-block'>
                <h4 className='card-title'>Zestawienia</h4>
                <div className='card-action mb-sm-0 my-2'>
                  <Nav className='nav nav-tabs' role='tablist'>
                    <Nav.Item className='nav-item'>
                        <Nav.Link	className='' data-toggle='tab'  role='tab' eventKey='reps'>Liczba powtórzeń/Ćwiczenie</Nav.Link>
                    </Nav.Item>
                    <Nav.Item className=''>
                      <Nav.Link
                        className='nav-link'
                        data-toggle='tab'             
                        role='tab'
                        eventKey='series'>
                        Liczba serii/Ćwiczenie
                      </Nav.Link>
                    </Nav.Item>
                    <Nav.Item className=''>
                      <Nav.Link
                        className='nav-link'
                        data-toggle='tab'             
                        role='tab'
                        eventKey='volume'>
                        Tonaż/Ćwiczenie
                      </Nav.Link>
                    </Nav.Item>
                  </Nav>
                </div>
              </div> 
              <div className='card-body'>
               
                <Tab.Content className='tab-content' id='myTabContent'>
                  <Tab.Pane eventKey='reps' id='user' role='tabpanel'>
                  {latestWorkoutLoaded &&  <BarChartt labelsArray={latestWorkout?.workoutParts?.map((item) => item.exerciseName)} dataArray = {prepareRepetitionsChartData()} labelMessage =  {'Łączna liczba wykonanych powtórzeń'}/>}
                  </Tab.Pane>
                  <Tab.Pane eventKey='series' id='user' role='tabpanel'>
                   {latestWorkoutLoaded &&  <BarChartt labelsArray={latestWorkout?.workoutParts?.map((item) => item.exerciseName)} dataArray = {prepareSeriesChartData()} labelMessage =  {'Łączna liczba wykonanych serii'}/>}
                  </Tab.Pane>
                 <Tab.Pane eventKey='volume' id='user' role='tabpanel'>
                 {latestWorkoutLoaded &&  <BarChartt labelsArray={latestWorkout?.workoutParts?.map((item) => item.exerciseName)} dataArray = {prepareVolumeChartData()} labelMessage =  {'Łączna objętość/tonaż '}/>}
                  </Tab.Pane> 
                </Tab.Content>
              </div>
            </Tab.Container>
          </div>
        </div>   }


        {!latestWorkoutLoaded && <Card>
                  <Card.Header className="d-block">
                     <Card.Title>Nie posiadasz żadnych treningów z aktualnie wybranej jednostki treningowej</Card.Title>
                     <Card.Text className="m-0 subtitle">
                        Rozpocznij trening uzupełniajać szablon treningowy wygenerowany na podstawie Twojego planu treningowego. Uzupełnij go i śledź swoje postępy. <br /> Poniżej znajdziesz najważniejsze wskazówki związane z poprawnie wykonanym treningiem<br />Udanego treningu !
                     </Card.Text>
                  </Card.Header>
                  <Card.Body>
                     <Accordion
                        className="accordion accordion-no-gutter accordion-header-bg"
                        defaultActiveKey="0"
                     >
                        {defaultAccordion.map((d, i) => (
                           <div className="accordion__item" key={i}>
                              <Accordion.Toggle
                                 as={Card.Text}
                                 eventKey={`${i}`}
                                 className={`accordion__header  ${
                                    activeWithoutSpace === i ? "" : "collapsed"
                                 }`}
                                 onClick={() =>
                                    setActiveWithoutSpace(
                                       activeWithoutSpace === i ? -1 : i
                                    )
                                 }
                              >
                                 <span className="accordion__header--text">
                                    {d.title}
                                 </span>
                                 <span className="accordion__header--indicator"></span>
                              </Accordion.Toggle>
                              <Accordion.Collapse eventKey={`${i}`}>
                                 <div className="accordion__body--text">
                                    {d.text}
                                 </div>
                              </Accordion.Collapse>
                           </div>
                        ))}
                     </Accordion>
                  </Card.Body>
               </Card> }


    </div>

 </div>
 
</div>


           <div className="row">



           <div className="col-lg-12">
                 <div className="card">
                    <div className="card-header">
                       <h4 className="card-title">Szkielet planu treningowe</h4>
                       <div className="mt-4">
                        <center>
                     <Button className="btn btn-primary mb-1 mr-1" onClick = {() => {saveWorkoutSession()}}>
                               Zapisz sesję treningową
                        </Button> 

                        <Button className="btn btn-primary mb-1 mr-1" onClick = {() => { cancelWorkoutSession()}}>
                               Anuluj
                        </Button> 
                        </center>
                        </div>
 
                       
                    </div>
                 

               <div className="card-body">
                     {!isLoading && template.trainingUnitParts.map((day, index) => {
                        return <WorkoutUnitTemplate trainingUnitPart = {day} updateCurrentSession = {handleUpdateCurrentSession} />
                     })}
                                            <div className="mt-4">
                        <center>
                     <Button className="btn btn-primary mb-1 mr-1" onClick = {() => {saveWorkoutSession()}}>
                               Zakończ trening i zapisz sesję treningową
                        </Button> 

                        <Button className="btn btn-primary mb-1 mr-1" onClick = {() => { cancelWorkoutSession()}}>
                               Anuluj
                        </Button> 
                        </center>
                        </div>
                    </div>  
                 </div>
              </div> 
           </div> 
                    
           </div>
          );
}