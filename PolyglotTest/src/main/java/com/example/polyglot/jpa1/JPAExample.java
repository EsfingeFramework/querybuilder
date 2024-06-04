package com.example.polyglot.jpa1;

import com.example.polyglot.entities.Temperatura;
import esfinge.querybuilder.core.Repository;
import esfinge.querybuilder.core.annotation.GreaterOrEquals;
import esfinge.querybuilder.core.annotation.LesserOrEquals;
import esfinge.querybuilder.core.annotation.TargetEntity;
import java.util.List;

@TargetEntity(Temperatura.class)
public interface JPAExample extends Repository<Temperatura> {

    List<Temperatura> getTemperatura();

    List<Temperatura> getTemperaturaByMes(String mes);

    List<Temperatura> getTemperaturaByMaximaOrderByMaximaAsc(@GreaterOrEquals double temp);

    List<Temperatura> getTemperaturaByMinimaOrderByMinimaDesc(@LesserOrEquals double temp);
}
