package br.edu.fa7.mobilepomodoro.repository;

import java.util.List;

/**
 * Created by erinaldo.souza on 02/06/2016.
 */
public interface IDao<T> {

    /**
     * Insere o Objeto do tipo T
     *
     * @since  02-06-2016
     *
     * @param t
     * @return id do o objeto inserido
     */
    public int create(T t);

    /**
     * Retorna todos os elementos do tipo T
     *
     * @since 02-06-2016
     * @return lsta de elementos T ou uma lista vazia caso não hajam dados.
     */
    public List<T> readAll();

    /**
     * Retorna o objeto com id igual ao parametro id
     *
     * @since 02-06-2016
     * @param id
     *
     * @return objeto encontrato ou nullo
     */
    public T read(Number id);
    /**
     * Atualiza o objeto com id igual ao parametro id, com os valores contidos no objeto T
     *
     * @since 02-06-2016
     * @param objeto
     *
     * @return id do objeto atualizado
     */
    public int update (T objeto);

    /**
     * Deleta o objeto com o ID igual ao passado por parametro
     *
     * @since 02-06-2016
     *
     * @param id do objeto a ser excluído
     * @return  id do objeto exluído
     */
    public int delete (Number id);

}