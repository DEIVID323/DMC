package com.example.DMC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DMC.model.TipoCambio;
import com.example.DMC.model.TipoCambioId;

public interface TipoCambioRepository extends JpaRepository<TipoCambio, TipoCambioId> {
}