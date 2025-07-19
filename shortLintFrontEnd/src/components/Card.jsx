import React from "react";
// eslint-disable-next-line no-unused-vars
import {motion} from "framer-motion";

/**
 * Card component displays a title and description inside an animated card.
 * Uses Framer Motion for entry animation.
 *
 * @param {Object} props - Component props.
 * @param {string} props.title - The title text to display.
 * @param {string} props.desc - The description text to display.
 * @returns {JSX.Element} Animated card element.
 */
const Card = ({title, desc}) => {
  return (<motion.div
      initial={{opacity: 0, y: 120}}
      whileInView={{
        opacity: 1, y: 0,
      }}
      viewport={{once: true}}
      transition={{duration: 0.5}}
      className="shadow-md shadow-slate-400 border flex flex-col px-4 py-8  gap-3 rounded-sm"
    >
      <h1 className="text-slate-900 text-xl font-bold ">{title}</h1>
      <p className="text-slate-700 text-sm"> {desc}</p>
    </motion.div>);
};

export default Card;