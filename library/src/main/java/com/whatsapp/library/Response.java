package com.whatsapp.library;

import java.util.Objects;
public class Response<T> {
      private T data;
      private boolean hasError;

      public Response() {
      }

      public Response(T data, boolean hasError) {
            this.data = data;
            this.hasError = hasError;
      }

      public T getData() {
            return this.data;
      }

      public void setData(T data) {
            this.data = data;
      }

      public boolean isHasError() {
            return this.hasError;
      }

      public boolean getHasError() {
            return this.hasError;
      }

      public void setHasError(boolean hasError) {
            this.hasError = hasError;
      }

      @Override
      public boolean equals(Object o) {
            if (o == this)
                  return true;
            if (!(o instanceof Response)) {
                  return false;
            }
            Response<T> response = (Response) o;
            return Objects.equals(data, response.data) && hasError == response.hasError;
      }
      
      @Override
      public String toString() {
            return "{" +
                        " data='" + getData() + "'" +
                        ", hasError='" + isHasError() + "'" +
                        "}";
      }

}