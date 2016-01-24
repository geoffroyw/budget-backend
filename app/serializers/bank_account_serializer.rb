class BankAccountSerializer < ActiveModel::Serializer
  attributes :id
  attributes :name, :currency_code
end
