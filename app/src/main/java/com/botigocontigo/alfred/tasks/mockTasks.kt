package com.botigocontigo.alfred.tasks

val tasksDB: ArrayList<Task> = arrayListOf(
        Task(
                1,
                "Regar las plantas del vecino",
                1,
                "dia",
                3,
                "hora",
                "Eric"
        ),
        Task(
                2,
                "Lavar la ropa de toda la semana",
                1,
                "semana",
                5,
                "dia",
                "Kevin"
        ),
        Task(
                3,
                "Cagar saldo en la SUBE",
                3,
                "dia",
                2,
                "dia",
                "Eze"
        ),
        Task(
                4,
                "Hacer gimnasia por 90min",
                2,
                "dia",
                1,
                "dia",
                "elgonza@gmail.com"
        ),
        Task(
                5,
                "Retirar los recibos de sueldo",
                1,
                "semana",
                4,
                "dia",
                "Eric"
        ),
        Task(
                6,
                "Prepararse para cocinar el asado",
                1,
                "mes",
                20,
                "dia",
                "Marta"
        )
)

var plansDB: ArrayList<Plan> = arrayListOf(
        Plan(1, "Plan de Administracion", arrayListOf(
                Task(
                        1,
                        "Regar las plantas del vecino",
                        1,
                        "dia",
                        3,
                        "hora",
                        "Eric"
                ),
                Task(
                        4,
                        "Hacer gimnasia por 90min",
                        2,
                        "dia",
                        1,
                        "dia",
                        "elgonza@gmail.com"
                ),
                Task(
                        6,
                        "Prepararse para cocinar el asado",
                        1,
                        "mes",
                        20,
                        "dia",
                        "Marta"
                )
        )),
        Plan(2, "Plan de Emprendimiento", arrayListOf(
                Task(
                        2,
                        "Lavar la ropa de toda la semana",
                        1,
                        "semana",
                        5,
                        "dia",
                        "Kevin"
                ),
                Task(
                        5,
                        "Retirar los recibos de sueldo",
                        1,
                        "semana",
                        4,
                        "dia",
                        "Eric"
                ),
                Task(
                6,
                "Prepararse para cocinar el asado",
                1,
                "mes",
                20,
                "dia",
                "Marta"
                )
        )),
        Plan(3, "Plan de Viaje", arrayListOf(
                Task(
                        1,
                        "Regar las plantas del vecino",
                        1,
                        "dia",
                        3,
                        "hora",
                        "Eric"
                ),
                Task(
                        2,
                        "Lavar la ropa de toda la semana",
                        1,
                        "semana",
                        5,
                        "dia",
                        "Kevin"
                ),
                Task(
                        3,
                        "Cagar saldo en la SUBE",
                        3,
                        "dia",
                        2,
                        "dia",
                        "Eze"
                )
        )),
        Plan(4, "Plan de Capacitacion", arrayListOf(
                Task(
                        1,
                        "Regar las plantas del vecino",
                        1,
                        "dia",
                        3,
                        "hora",
                        "Eric"
                ),
                Task(
                        2,
                        "Lavar la ropa de toda la semana",
                        1,
                        "semana",
                        5,
                        "dia",
                        "Kevin"
                ),
                Task(
                        4,
                        "Hacer gimnasia por 90min",
                        2,
                        "dia",
                        1,
                        "dia",
                        "elgonza@gmail.com"
                ),
                Task(
                        5,
                        "Retirar los recibos de sueldo",
                        1,
                        "semana",
                        4,
                        "dia",
                        "Eric"
                ),
                Task(
                        6,
                        "Prepararse para cocinar el asado",
                        1,
                        "mes",
                        20,
                        "dia",
                        "Marta"
                )
        ))
)