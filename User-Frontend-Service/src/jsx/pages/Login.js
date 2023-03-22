import React,{ useState } from "react";
import { connect, useDispatch } from 'react-redux';
import { Link } from "react-router-dom";
import {
    loadingToggleAction,
    loginAction,
} from '../../store/actions/AuthActions';
import loginbg from "../../images/loginbg.jpg";

function Login (props) {
  const [username, setUsername] = useState('');
    let errorsObj = { username: '', password: '' };
    const [errors, setErrors] = useState(errorsObj);
    const [password, setPassword] = useState('');

    const dispatch = useDispatch();

    function onLogin(e) {
        e.preventDefault();
        let error = false;
        const errorObj = { ...errorsObj };
        if (username === '') {
            errorObj.userName = 'Nazwa użytkownika jest wymagana';
            error = true;
        }
        if (password === '') {
            errorObj.password = 'Hasło jest wymagane';
            error = true;
        }
        setErrors(errorObj);
        if (error) {
			return ;
		}
		dispatch(loadingToggleAction(true));	
        dispatch(loginAction(username, password, props.history));
    }

  return (
		<div className="login-main-page" style={{backgroundImage:"url("+ loginbg +")"}}>
            <div className="login-wrapper">

                <div className="login-aside-left">
                    <div className="row m-0 justify-content-center h-100 align-items-center">
                      <div className="p-5">
                        <div className="authincation-content">
                          <div className="row no-gutters">
                            <div className="col-xl-12">
                              <div className="auth-form-1">
                                <div className="mb-4">
                                    <h3 className="dz-title mb-1">Logowanie</h3>
                                    <p className="">Posiadasz już konto w aplikacji ?</p>
                                </div>
                                {props.errorMessage && (
                                    <div className='bg-red-300 text-red-900 border border-red-900 p-1 my-2'>
                                        {props.errorMessage}
                                    </div>
                                )}
                                {props.successMessage && (
                                    <div className='bg-green-300 text-green-900 border border-green-900 p-1 my-2'>
                                        {props.successMessage}
                                    </div>
                                )}
                                <form onSubmit={onLogin}>
                                    <div className="form-group">
                                        <label className="mb-2 ">
                                          <strong>Login</strong>
                                        </label>
                                        <input type="text" className="form-control"
										                	  value={username}
                                           onChange={(e) => setUsername(e.target.value)}
										                     placeholder="..."
                                        />
                                      {errors.username && <div className="text-danger fs-12">{errors.username}</div>}
                                    </div>
                                    <div className="form-group">
                                        <label className="mb-2 "><strong>Hasło</strong></label>
                                        <input
                                          type="password"
                                          className="form-control"
                                          value={password}
										  placeholder="..."
                                            onChange={(e) =>
                                                setPassword(e.target.value)
                                            }
                                        />
                                        {errors.password && <div className="text-danger fs-12">{errors.password}</div>}
                                    </div>
                                  <div className="text-center">
                                    <button
                                      type="submit"
                                      className="btn btn-primary btn-block"
                                    >
                                      Zaloguj się
                                    </button>
                                  </div>
                                </form>
                                <div className="new-account mt-2">
                                  <p className="">
                                    Nie posiadasz konta ?{" "}
                                    <Link className="text-primary" to="./register">
                                      Zarejestruj się
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
        </div>
	);
}

const mapStateToProps = (state) => {
    return {
        errorMessage: state.auth.errorMessage,
        successMessage: state.auth.successMessage,
        showLoading: state.auth.showLoading,
    };
};
export default connect(mapStateToProps)(Login);
