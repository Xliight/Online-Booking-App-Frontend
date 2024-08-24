import { useRef, useState } from "react";
import PropTypes from "prop-types";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import Notification from "./Notification";
import Swal from "sweetalert2";
import.meta.env.REACT_APP_API_URL
const SignUpAndSignIn = ({ isInLoggedInPage }) => {
  const navigate = useNavigate();
  
  let [nameInputValue, setNameInputValue] = useState("");
  let [emailInputValue, setEmailInputValue] = useState("");
  let [passwordInputValue, setPasswordInputValue] = useState("");
  let [confirmPassInputValue, setConfirmPassInputValue] = useState("");

  let nameInputRef = useRef();
  let emailInputRef = useRef();
  let passwordInputRef = useRef();
  let confirmPasswordInputRef = useRef();

  let nameMessageRef = useRef();
  let emailMessageRef = useRef();
  let passwordMessageRef = useRef();

  const emailRegEx = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  const passwordRegEx =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/;

  let [errors, setErrors] = useState([]);

  let nameInputFunc = () => {
    // if in logIn page return false to not do the condition
    // else return if the nameInput is empty or not
    return isInLoggedInPage ? false : nameInputValue.trim().length == 0;
  };

  const handleSubmit = () => {
    if (
      emailInputValue.trim().length == 0 ||
      nameInputFunc() ||
      passwordInputValue.trim().length == 0
    ) {
      // focus on the empty field
      if (nameInputValue.trim().length == 0) {
        console.log(nameInputValue.trim().length == 0);
        nameInputRef.current.focus();
      } else if (emailInputValue.trim().length == 0) {
        console.log("email");
        emailInputRef.current.focus();
      } else if (passwordInputValue.trim().length == 0) {
        console.log("pass");
        passwordInputRef.current.focus();
      } else if (confirmPassInputValue.trim().length == 0) {
        confirmPasswordInputRef.current.focus();
      }
      // if there is an empty field
      setErrors((prevErrors) => [...prevErrors, "Empty Fields is forbidden"]);
    } else if (!emailRegEx.test(emailInputValue)) {
      // if the email pattern isn't correct
      console.log("fsdf");
      emailInputRef.current.focus();
      setErrors((prevErrors) => [...prevErrors, "Please Write A Valid Email"]);
    } else if (!passwordRegEx.test(passwordInputValue)) {
      // if the password pattern isn't correct
      passwordInputRef.current.focus();
      setErrors((prevErrors) => [
        ...prevErrors,
        validatePassword(passwordInputValue),
      ]);
    } else if (
      isInLoggedInPage ? false : passwordInputValue != confirmPassInputValue
    ) {
      // if the password field and confirm password fields aren't matched
      confirmPasswordInputRef.current.focus();
      setErrors((prevErrors) => [
        ...prevErrors,
        "Confirm Passowrd Field Isn't Matched To Passowrd Field",
      ]);
    } else {
      // if everything is correct
      if (isInLoggedInPage) {
        // if in logIn Page
        console.log(`here: ${process.env.REACT_APP_API_URL}`)
        axios
          .post(`${process.env.REACT_APP_API_URL}/api/v1/auth/login`, {
            email: emailInputValue,
            password: passwordInputValue,
          })
          .then((res) => {
            // make an alert of successfull logIn response and navigate the user to Home Page

            // and store the token in local storage
            localStorage.setItem("token", res.data.access_token);

            Swal.fire({
              title: "Good job!",
              text: "You Successfully LogedIn Now You Can Book Rooms",
              icon: "success",
            });

            // Make It Naviagte To Home
            // navigate("/");
          })
          .catch((error) => {
            // make an error message on unsuccessfull logIn
            if (error.response.data == "Authentication failed .") {
              setErrors((prevErrors) => [
                ...prevErrors,
                "Error in Email Or Password",
              ]);
            } else {
              setErrors((prevErrors) => [...prevErrors, error.response.data]);
            }
          });
      } else {
        // if in signUp Page
        axios
          .post(`${process.env.REACT_APP_API_URL}/api/v1/auth/register`, {
            firstname: nameInputValue,
            email: emailInputValue,
            password: passwordInputValue,
            role: "USER",
          })
          .then(() => {
            // make an alert of successfull register response and navigate the user to logIn Page
            Swal.fire({
              title: "Good job!",
              text: "You Successfully Registere Now You Have To LogIn!",
              icon: "success",
            });
            navigate("/logIn");
          })
          .catch((error) => {
            // make an error message on unsuccessfull register
            setErrors((prevErrors) => [...prevErrors, error.response.data]);
          });
      }
    }
  };

  // password validation function that return the issue with the password
  const validatePassword = (password) => {
    if (password.length < 8) {
      return "Password must be at least 8 characters long.";
    } else if (!/[a-z]/.test(password)) {
      return "Password must contain at least one lowercase letter.";
    } else if (!/[A-Z]/.test(password)) {
      return "Password must contain at least one uppercase letter.";
    } else if (!/[0-9]/.test(password)) {
      return "Password must contain at least one digit.";
    } else if (!/[!@#$%^&*]/.test(password)) {
      return "Password must contain at least one special character (!@#$%^&*).";
    } else if (!passwordRegEx.test(password)) {
      return "Password is invalid.";
    } else {
      return "Password is invalid.";
    }
  };

  const handleInputs = () => {
    if (!isInLoggedInPage) {
      // if isn't in logIn Page

      if (nameInputValue.length > 0) {
        // if the name field isn't empty remove the required message
        nameMessageRef.current.classList.add("hidden");
      } else {
        // if the name field is empty add the required message
        nameMessageRef.current.classList.remove("hidden");
      }
    }

    if (emailInputValue.length > 0) {
      // if the email field isn't empty remove the required message
      emailMessageRef.current.classList.add("hidden");
    } else {
      // if the email field is empty add the required message
      emailMessageRef.current.classList.remove("hidden");
    }

    if (passwordInputValue.length > 0) {
      // if the password field isn't empty remove the required message
      passwordMessageRef.current.classList.add("hidden");
    } else {
      // if the password field is empty add the required message
      passwordMessageRef.current.classList.remove("hidden");
    }
  };



  return (
    <div className="bg-[#f9f9f9] min-h-screen relative overflow-x-hidden">
      <section className="container py-[70px]">
        <div className="max-w-lg mx-auto bg-white py-16 px-12 text-center rounded-lg shadow-signUp">
          <div className="mb-6">
            <h1 className="text-[28px] text-[#313443] font-bold mb-5">
              {isInLoggedInPage ? "Sign In" : "Signup"}
            </h1>
            <p className="text-[#7d7572] text-[14px]">
              {isInLoggedInPage
                ? "Sign in to your account"
                : "Signup your account"}
            </p>
          </div>
          <form className="flex flex-col gap-5 mb-8" action="">
            {!isInLoggedInPage && (
              <div className="relative">
                <input
                  ref={nameInputRef}
                  value={nameInputValue}
                  onChange={(e) => {
                    handleInputs();
                    setNameInputValue(e.target.value);
                  }}
                  onBlur={handleInputs}
                  className="w-full pt-4 pb-3 px-[13px] text-[13px] rounded border border-[#c4c4c4] hover:border-black focus:outline-blue-800"
                  type="text"
                  placeholder="Full Name"
                />
                <span className="pr-[5px] bg-white absolute top-[0] translate-y-[-50%] left-[15px] z-10 text-[#7d7572] text-[13px]">
                  Name
                </span>
                <span
                  ref={nameMessageRef}
                  className="text-[#687693] float-left hidden"
                >
                  The full name field is required.
                </span>
              </div>
            )}

            <div className="relative">
              <input
                ref={emailInputRef}
                value={emailInputValue}
                onChange={(e) => {
                  handleInputs();
                  setEmailInputValue(e.target.value);
                }}
                onBlur={handleInputs}
                className="w-full pt-4 pb-3 px-[13px] text-[13px] rounded border border-[#c4c4c4]  hover:border-black focus:outline-blue-800"
                type="email"
                placeholder="E-mail"
              />
              <span className="pr-[5px] bg-white absolute top-[0] translate-y-[-50%] left-[15px] z-10 text-[#7d7572] text-[13px]">
                E-mail
              </span>
              <span
                ref={emailMessageRef}
                className="text-[#687693] float-left hidden"
              >
                The email field is required.
              </span>
            </div>

            <div className="relative">
              <input
                ref={passwordInputRef}
                value={passwordInputValue}
                onChange={(e) => {
                  handleInputs();
                  setPasswordInputValue(e.target.value);
                }}
                onBlur={handleInputs}
                className="w-full pt-4 pb-3 px-[13px] text-[13px] rounded border border-[#c4c4c4]  hover:border-black focus:outline-blue-800"
                type="password"
                placeholder="Password"
              />
              <span className="pr-[5px] bg-white absolute top-[0] translate-y-[-50%] left-[15px] z-10 text-[#7d7572] text-[13px]">
                Password
              </span>
              <span
                ref={passwordMessageRef}
                className="text-[#687693] float-left hidden"
              >
                The password field is required.
              </span>
            </div>

            {!isInLoggedInPage && (
              <div className="relative">
                <input
                  className="w-full pt-4 pb-3 px-[13px] text-[13px] rounded border border-[#c4c4c4]  hover:border-black focus:outline-blue-800"
                  type="password"
                  value={confirmPassInputValue}
                  onChange={(e) => {
                    setConfirmPassInputValue(e.target.value);
                  }}
                  ref={confirmPasswordInputRef}
                  placeholder="Confirm Password"
                />
                <span className="pr-[5px] bg-white absolute top-[0] translate-y-[-50%] left-[15px] z-10 text-[#7d7572] text-[13px]">
                  Confirm Password
                </span>
              </div>
            )}
          </form>
          {isInLoggedInPage && (
            <div className="flex justify-between mb-8">
              <div className="flex items-center gap-3">
                <input
                  className="cursor-pointer w-5 h-5 accent-[#f40058] rounded border-2"
                  type="checkbox"
                  id="checkbox"
                />
                <label
                  className="select-none cursor-pointer text-[#687693]"
                  htmlFor="checkbox"
                >
                  Remember Me
                </label>
              </div>
              <div className="text-[#0774ad]">Forgot Password?</div>
            </div>
          )}
          <button
            onClick={() => {
              handleSubmit();
            }}
            className="uppercase bg-[#fc4c4c] text-white w-full py-2 rounded text-[13px] mb-5"
          >
            {isInLoggedInPage ? "log in" : "sign up"}
          </button>
          <div className="flex gap-3 w-[76%] m-auto">
            <div className="bg-[#3b5999] grow flex justify-center items-center py-2 rounded">
              <img
                className="invert w-4 h-4"
                src="../../imgs/facebook-app-symbol.png"
                alt=""
              />
            </div>

            <div className="bg-[#55acef] grow flex justify-center items-center py-2 rounded">
              <img
                className="invert w-4 h-4"
                src="../../imgs/twitter.png"
                alt=""
              />
            </div>

            <div className="bg-[#0077b4] grow flex justify-center items-center py-2 rounded">
              <img
                className="invert w-4 h-4"
                src="../../imgs/linkedin.png"
                alt=""
              />
            </div>
          </div>
          <div className="flex justify-center gap-4 mt-7 text-[14px]">
            <div className="text-[#7d7572]">
              {isInLoggedInPage
                ? "Don't have an account?"
                : "Already have an account?"}
            </div>
            <Link
              className="text-[#0774ad]"
              to={isInLoggedInPage ? "/" : "/logIn"}
            >
              {isInLoggedInPage ? " Create free account" : "Return to Sign In" }
            </Link>
          </div>
        </div>
      </section>

      <div className="fixed top-[50px] right-0 flex flex-col gap-6">
        {errors.map((error, index) => {
          return <Notification key={index} text={error} />;
        })}
      </div>
    </div>
  );
};

SignUpAndSignIn.propTypes = {
  isInLoggedInPage: PropTypes.bool,
};

export default SignUpAndSignIn;
