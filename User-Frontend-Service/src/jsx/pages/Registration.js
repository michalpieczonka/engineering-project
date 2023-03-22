import React, { useState } from 'react'
import { Link } from 'react-router-dom'
import { connect, useDispatch } from 'react-redux';
//import logo from '../../images/logo-full.png'
import Loader from '../pages/Loader/Loader';
import {
    loadingToggleAction,
    signupAction,
} from '../../store/actions/AuthActions';
import Select from "react-select";
import {Button} from "react-bootstrap"
import {DatePicker } from "@y0c/react-datepicker";

function Register(props) {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    let errorsObj = { email: '', password: '', other: '' };
    const [errors, setErrors] = useState(errorsObj);
    const [password, setPassword] = useState('');

    const[gender, setGender] = useState('')
    const [dateOfBirth, setDateOfBirth] = useState(new Date())
    const [trainSince, setTrainSince] = useState(new Date())

    let genderOptions = 
    [
       {value: 'MALE', label: "Mężczyzna"},
       {value: 'FEMALE', label: "Kobieta"} 
    ]

    const dispatch = useDispatch();

    const signUp = () => {

        let error = false;
        const errorObj = { ...errorsObj };
        if (email === '') {
            errorObj.email = 'Poprawny email jest wymagany';
            error = true;
        }

        if (password === '') {
            errorObj.password = 'Hasło jest wymagane';
            error = true;
        }

        if(username === ''){
            errorObj.password = 'Nazwa użytkownika jest wymagana';
            error = true;
        }
        

        setErrors(errorObj);

        if (error) return;
        dispatch(loadingToggleAction(true));

        dispatch(signupAction(username, email, password, gender.value, dateOfBirth, trainSince, props.history));
    }
  return (
   <div className="authincation h-100 p-meddle">
         <div className="container h-100">
            <div className="row justify-content-center h-100 align-items-center">
               <div className="col-md-6">
                  <div className="authincation-content">
                     <div className="row no-gutters">
                            <div className='col-xl-12'>
                                {props.showLoading && <Loader />}
                                <div className='auth-form'>
                                   <h4 className="text-center mb-4">Workout-Helper - rejestracja</h4>
                                    {props.errorMessage && (
                                        <div className='bg-red-300 text-danger border border-red-900 p-1 my-2'>
                                            {props.errorMessage}
                                        </div>
                                    )}
                                    {props.successMessage && (
                                        <div className='bg-green-300 text-danger text-green-900  p-1 my-2'>
                                            {props.successMessage}
                                        </div>
                                    )}
                                    <form >
                                        <div className='form-group'>
                                            <label className='mb-1'>
                                              <strong>Nazwa użytkownika</strong>
                                            </label>
                                            <input type="username" className="form-control"
                                                   value={username}
                                                   onChange={(e) =>
                                                       setUsername(e.target.value)
                                                   }
                                                   placeholder='...'
                                            />
                                        </div>
                                        <div className='form-group'>
                                            <label className='mb-1'>
                                              <strong>Adres email</strong>
                                            </label>
                                            <input type="email" className="form-control"
                                                value={email}
                                                onChange={(e) => setEmail(e.target.value)}
												placeholder='...'
                                            />
                                            {errors.email && <div className="text-danger fs-12">{errors.email}</div>}
                                        </div>
                                        <div className='form-group'>
                                            <label className='mb-1'>
                                              <strong>Hasło</strong>
                                            </label>
                                            <input type="password" className="form-control"
                                                value={password}
                                                onChange={(e) =>
                                                    setPassword(e.target.value)
                                                }
                                            />
                                        </div>
                                        
                                        <div className='form-group'>
                                            <label className='mb-1'>
                                              <strong>Data urodzenia</strong>
                                            </label>
                                            <DatePicker selected={dateOfBirth} 
                                              onChange={date => setDateOfBirth(date.$d.toLocaleDateString())} />
                                            {errors.other && <div className="text-danger fs-12">{errors.other}</div>}
                                        </div>

                                        <div className='form-group'>
                                            <label className='mb-1'>
                                              <strong>Płeć</strong>
                                            </label>
                                            <Select
                                                 value={gender}
                                                 onChange={(e) => setGender(e)}
                                                 options={genderOptions}
                        style={{
                           lineHeight: "40px",
                           color: "#7e7e7e",
                           paddingLeft: " 15px",
                        }}  />
                                            {errors.email && <div className="text-danger fs-12">{errors.email}</div>}
                                        </div>

                                        <div className='form-group'>
                                            <label className='mb-1'>
                                              <strong>Data rozpoczęcia treningów</strong>
                                            </label>
                                            <DatePicker selected={trainSince} 
                                              onChange={date => setTrainSince(date.$d.toLocaleDateString())} />
                                            {errors.other && <div className="text-danger fs-12">{errors.other}</div>}
                                        </div>

                                        
                                        
                                        {errors.password && <div className="text-danger fs-12">{errors.password}</div>}
                                        <div className='text-center mt-4'>
                                            {/* <input type='submit' className='btn btn-primary btn-block'/> */}
                                            <Button onClick = {signUp}> Zarejestruj sie </Button>
                                        </div>
                                    </form>
                                    <div className='new-account mt-3 text-black'>
                                        <p>
                                            Posiadasz już konto ?{' '}
                                            <Link className='text-primary' to='/login'>
                                                Zaloguj się
                                            </Link>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
  )
}

const mapStateToProps = (state) => {
    return {
        errorMessage: state.auth.errorMessage,
        successMessage: state.auth.successMessage,
        showLoading: state.auth.showLoading,
    };
};

export default connect(mapStateToProps)(Register);
