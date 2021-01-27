///*
// * Copyright 2021 Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package digital.inception.persistence.converter;
//
//
//
//import java.nio.ByteBuffer;
//import java.util.UUID;
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//
///**
// * The <code>UUIDConverter</code> class implements the custom JPA converter for the UUID type.
// *
// * <p>NOTE: This is required for EclipseLink.
// *
// * @author Marcus Portmann
// */
//@Converter(autoApply = true)
//public class UUIDConverter implements AttributeConverter<UUID, Object> {
//
//  /**
//   * Converts the value stored in the entity attribute into the data representation to be stored in
//   * the database.
//   *
//   * @param attribute the entity attribute value to be converted
//   * @return the converted data to be stored in the database column
//   */
//  @Override
//  public byte[] convertToDatabaseColumn(UUID attribute) {
//    if (attribute == null) return null;
//    byte[] buffer = new byte[16];
//    ByteBuffer bb = ByteBuffer.wrap(buffer);
//    bb.putLong(attribute.getMostSignificantBits());
//    bb.putLong(attribute.getLeastSignificantBits());
//    return buffer;
//  }
//
//  /**
//   * Converts the data stored in the database column into the value to be stored in the entity
//   * attribute. Note that it is the responsibility of the converter writer to specify the correct
//   * dbData type for the corresponding column for use by the JDBC driver: i.e., persistence
//   * providers are not expected to do such type conversion.
//   *
//   * @param dbData the data from the database column to be converted
//   * @return the converted value to be stored in the entity attribute
//   */
//  @Override
//  public UUID convertToEntityAttribute(Object dbData) {
//    if (dbData == null) return null;
//
//    if (dbData instanceof UUID) {
//      return (UUID) dbData;
//    } else if (dbData instanceof byte[]) {
//      ByteBuffer bb = ByteBuffer.wrap((byte[]) dbData);
//      long high = bb.getLong();
//      long low = bb.getLong();
//      return new UUID(high, low);
//    }
//
//    throw new RuntimeException(
//        "Failed to convert the UUID database data ("
//            + dbData
//            + ") of type ("
//            + dbData.getClass().getTypeName()
//            + ") to a UUID");
//  }
//}
