import React from "react";
import { motion } from "framer-motion";

const Card = ({ title, desc }) => (
  <motion.div
    initial={{ opacity: 0, y: 120 }}
    whileInView={{ opacity: 1, y: 0 }}
    viewport={{ once: false, amount: 0.3 }}
    transition={{ duration: 0.5 }}
    className="border shadow-md shadow-slate-400 flex flex-col gap-3 px-4 py-8 rounded-sm"
  >
    <h1 className="text-xl font-bold text-slate-900">{title}</h1>
    <p className="text-sm text-slate-700">{desc}</p>
  </motion.div>
);
export default Card;