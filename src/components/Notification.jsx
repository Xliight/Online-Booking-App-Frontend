import PropTypes from "prop-types";
import { useEffect, useRef } from "react";

const Notification = ({ text }) => {
  const notificationRef = useRef();
  const handleCloseBtn = (e) => {
    if (e) {
      // make the alert message move right on clicking close button
      e.target.parentElement.classList.remove("animate-moveLeft");
      e.target.parentElement.classList.add("animate-moveRight");
      setTimeout(() => {
        // then remove the alert message after the animation ends and the message is fully unvisible
        e.target.parentElement.classList.add("hidden");
      }, 600);
    }
  };

  useEffect(() => {
    let seondTiemr;
    let firtTimer = setTimeout(() => {
      // make the alert message move right after 5s when the loading bar is finished and is empty
      notificationRef.current.classList.remove("animate-moveLeft");
      notificationRef.current.classList.add("animate-moveRight");
      seondTiemr = setTimeout(() => {
        // then remove the alert message after the animation ends and the message is fully unvisible
        notificationRef.current.classList.add("hidden");
      }, 600);
    }, 5000);

    return () => {
      clearTimeout(firtTimer);
      clearTimeout(seondTiemr);
    };
  }, []);

  return (
    <div
      ref={notificationRef}
      className="relative px-3 py-5 animate-moveLeft max-w-[250px] rounded-t-lg duration-600 bg-white shadow-signUp flex items-center gap-2"
    >
      <div className="absolute rounded-b-lg h-1 bottom-[-4px] left-0 animate-loading bg-[#fc4c4c]"></div>
      <div className="h-5 w-5 shrink-0 bg-[#fc4c4c] rounded-full text-white flex justify-center items-center">
        !
      </div>
      <div className="text-balance">{text}</div>
      <div
        onClick={handleCloseBtn}
        className="h-3 w-3 absolute top-2 right-2 cursor-pointer before:absolute before:w-full before:h-[1px] before:content-[''] before:rotate-45 before:top-1/2 before:left-0 before:translate-y-[-50%] after:absolute after:w-full after:h-[1px] after:content-[''] after:rotate-[-45deg] after:top-1/2 after:left-0 after:translate-y-[-50%] before:bg-black after:bg-black"
      ></div>
    </div>
  );
};

Notification.propTypes = {
  text: PropTypes.string,
};

export default Notification;
