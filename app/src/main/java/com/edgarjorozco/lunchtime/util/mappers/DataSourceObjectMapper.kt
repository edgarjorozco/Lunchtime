package com.edgarjorozco.lunchtime.util.mappers

abstract class DataSourceObjectMapper<DataSourceObject, DomainModel> {
    abstract fun mapFromDataSourceObj(dataSourceObject: DataSourceObject): DomainModel
    abstract fun mapToDataSourceObj(domainModel: DomainModel): DataSourceObject
    open fun mapFromDataSourceObjList(dataSourceObjList: List<DataSourceObject>): List<DomainModel> {
        return dataSourceObjList.map { mapFromDataSourceObj(it) }
    }
    open fun mapToDataSourceObjList(domainModelList: List<DomainModel>): List<DataSourceObject> {
        return domainModelList.map { mapToDataSourceObj(it) }
    }
}