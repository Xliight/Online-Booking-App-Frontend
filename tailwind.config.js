/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      keyframes: {
        moveRight: {
          "0%": { transform: "translateX(-10%)" },
          "100%": { transform: "translateX(100%)" },
        },
        moveLeft: {
          "0%": { transform: "translateX(100%)" },
          "100%": { transform: "translateX(-10%)" },
        },
        loading: {
          "0%": { width: "100%", right: "0" },
          "100%": { width: "0%", right: "0" },
        },
      },
      animation: {
        moveLeft: "moveLeft 0.6s ease-in-out forwards",
        moveRight: "moveRight 0.6s ease-in-out forwards",
        loading: "loading 5s linear forwards",
      },
      boxShadow: {
        signUp: "rgba(100, 100, 111, 0.2) 0px 7px 29px 0px",
      },
    },
  },
  plugins: [],
};
